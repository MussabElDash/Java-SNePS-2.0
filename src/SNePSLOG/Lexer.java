package SNePSLOG;

import java_cup.runtime.Symbol;

class Lexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Lexer(java.io.Reader reader) {
		this();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Lexer(java.io.InputStream instream) {
		this();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(
				instream));
	}

	private Lexer() {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = { 0 };

	private void yybegin(int state) {
		yy_lexical_state = state;
	}

	private int yy_advance() throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer, yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer, yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}

	private void yy_move_end() {
		if (yy_buffer_end > yy_buffer_start
				&& '\n' == yy_buffer[yy_buffer_end - 1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start
				&& '\r' == yy_buffer[yy_buffer_end - 1])
			yy_buffer_end--;
	}

	private boolean yy_last_was_cr = false;

	private void yy_mark_start() {
		yy_buffer_start = yy_buffer_index;
	}

	private void yy_mark_end() {
		yy_buffer_end = yy_buffer_index;
	}

	private void yy_to_mark() {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start)
				&& ('\r' == yy_buffer[yy_buffer_end - 1]
						|| '\n' == yy_buffer[yy_buffer_end - 1]
						|| 2028/* LS */== yy_buffer[yy_buffer_end - 1] || 2029/* PS */== yy_buffer[yy_buffer_end - 1]);
	}

	private java.lang.String yytext() {
		return (new java.lang.String(yy_buffer, yy_buffer_start, yy_buffer_end
				- yy_buffer_start));
	}

	private int yylength() {
		return yy_buffer_end - yy_buffer_start;
	}

	private char[] yy_double(char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2 * buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}

	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = { "Error: Internal error.\n",
			"Error: Unmatched input.\n" };

	private void yy_error(int code, boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}

	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i = 0; i < size1; i++) {
			for (int j = 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex == -1) ? st : st.substring(0,
						commaIndex);
				st = st.substring(commaIndex + 1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j] = Integer.parseInt(workString);
					continue;
				}
				lengthString = workString.substring(colonIndex + 1);
				sequenceLength = Integer.parseInt(lengthString);
				workString = workString.substring(0, colonIndex);
				sequenceInteger = Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}

	private int yy_acpt[] = {
	/* 0 */YY_NOT_ACCEPT,
	/* 1 */YY_NO_ANCHOR,
	/* 2 */YY_NO_ANCHOR,
	/* 3 */YY_NO_ANCHOR,
	/* 4 */YY_NO_ANCHOR,
	/* 5 */YY_NO_ANCHOR,
	/* 6 */YY_NO_ANCHOR,
	/* 7 */YY_NO_ANCHOR,
	/* 8 */YY_NO_ANCHOR,
	/* 9 */YY_NO_ANCHOR,
	/* 10 */YY_NO_ANCHOR,
	/* 11 */YY_NO_ANCHOR,
	/* 12 */YY_NO_ANCHOR,
	/* 13 */YY_NO_ANCHOR,
	/* 14 */YY_NO_ANCHOR,
	/* 15 */YY_NO_ANCHOR,
	/* 16 */YY_NO_ANCHOR,
	/* 17 */YY_NO_ANCHOR,
	/* 18 */YY_NO_ANCHOR,
	/* 19 */YY_NO_ANCHOR,
	/* 20 */YY_NO_ANCHOR,
	/* 21 */YY_NO_ANCHOR,
	/* 22 */YY_NO_ANCHOR,
	/* 23 */YY_NO_ANCHOR,
	/* 24 */YY_NO_ANCHOR,
	/* 25 */YY_NO_ANCHOR,
	/* 26 */YY_NO_ANCHOR,
	/* 27 */YY_NO_ANCHOR,
	/* 28 */YY_NO_ANCHOR,
	/* 29 */YY_NO_ANCHOR,
	/* 30 */YY_NO_ANCHOR,
	/* 31 */YY_NO_ANCHOR,
	/* 32 */YY_NO_ANCHOR,
	/* 33 */YY_NO_ANCHOR,
	/* 34 */YY_NO_ANCHOR,
	/* 35 */YY_NO_ANCHOR,
	/* 36 */YY_NO_ANCHOR,
	/* 37 */YY_NO_ANCHOR,
	/* 38 */YY_NO_ANCHOR,
	/* 39 */YY_NO_ANCHOR,
	/* 40 */YY_NO_ANCHOR,
	/* 41 */YY_NO_ANCHOR,
	/* 42 */YY_NO_ANCHOR,
	/* 43 */YY_NO_ANCHOR,
	/* 44 */YY_NO_ANCHOR,
	/* 45 */YY_NO_ANCHOR,
	/* 46 */YY_NO_ANCHOR,
	/* 47 */YY_NO_ANCHOR,
	/* 48 */YY_NO_ANCHOR,
	/* 49 */YY_NO_ANCHOR,
	/* 50 */YY_NO_ANCHOR,
	/* 51 */YY_NO_ANCHOR,
	/* 52 */YY_NO_ANCHOR,
	/* 53 */YY_NO_ANCHOR,
	/* 54 */YY_NO_ANCHOR,
	/* 55 */YY_NO_ANCHOR,
	/* 56 */YY_NO_ANCHOR,
	/* 57 */YY_NO_ANCHOR,
	/* 58 */YY_NO_ANCHOR,
	/* 59 */YY_NO_ANCHOR,
	/* 60 */YY_NO_ANCHOR,
	/* 61 */YY_NO_ANCHOR,
	/* 62 */YY_NO_ANCHOR,
	/* 63 */YY_NO_ANCHOR,
	/* 64 */YY_NO_ANCHOR,
	/* 65 */YY_NO_ANCHOR,
	/* 66 */YY_NO_ANCHOR,
	/* 67 */YY_NO_ANCHOR,
	/* 68 */YY_NO_ANCHOR,
	/* 69 */YY_NO_ANCHOR,
	/* 70 */YY_NO_ANCHOR,
	/* 71 */YY_NO_ANCHOR,
	/* 72 */YY_NO_ANCHOR,
	/* 73 */YY_NO_ANCHOR,
	/* 74 */YY_NO_ANCHOR,
	/* 75 */YY_NO_ANCHOR,
	/* 76 */YY_NO_ANCHOR,
	/* 77 */YY_NO_ANCHOR,
	/* 78 */YY_NOT_ACCEPT,
	/* 79 */YY_NO_ANCHOR,
	/* 80 */YY_NO_ANCHOR,
	/* 81 */YY_NO_ANCHOR,
	/* 82 */YY_NOT_ACCEPT,
	/* 83 */YY_NO_ANCHOR,
	/* 84 */YY_NO_ANCHOR,
	/* 85 */YY_NOT_ACCEPT,
	/* 86 */YY_NO_ANCHOR,
	/* 87 */YY_NOT_ACCEPT,
	/* 88 */YY_NO_ANCHOR,
	/* 89 */YY_NOT_ACCEPT,
	/* 90 */YY_NO_ANCHOR,
	/* 91 */YY_NOT_ACCEPT,
	/* 92 */YY_NO_ANCHOR,
	/* 93 */YY_NOT_ACCEPT,
	/* 94 */YY_NO_ANCHOR,
	/* 95 */YY_NOT_ACCEPT,
	/* 96 */YY_NO_ANCHOR,
	/* 97 */YY_NOT_ACCEPT,
	/* 98 */YY_NO_ANCHOR,
	/* 99 */YY_NOT_ACCEPT,
	/* 100 */YY_NO_ANCHOR,
	/* 101 */YY_NOT_ACCEPT,
	/* 102 */YY_NO_ANCHOR,
	/* 103 */YY_NOT_ACCEPT,
	/* 104 */YY_NO_ANCHOR,
	/* 105 */YY_NOT_ACCEPT,
	/* 106 */YY_NO_ANCHOR,
	/* 107 */YY_NOT_ACCEPT,
	/* 108 */YY_NO_ANCHOR,
	/* 109 */YY_NOT_ACCEPT,
	/* 110 */YY_NO_ANCHOR,
	/* 111 */YY_NOT_ACCEPT,
	/* 112 */YY_NO_ANCHOR,
	/* 113 */YY_NOT_ACCEPT,
	/* 114 */YY_NO_ANCHOR,
	/* 115 */YY_NOT_ACCEPT,
	/* 116 */YY_NO_ANCHOR,
	/* 117 */YY_NOT_ACCEPT,
	/* 118 */YY_NO_ANCHOR,
	/* 119 */YY_NOT_ACCEPT,
	/* 120 */YY_NO_ANCHOR,
	/* 121 */YY_NOT_ACCEPT,
	/* 122 */YY_NO_ANCHOR,
	/* 123 */YY_NOT_ACCEPT,
	/* 124 */YY_NO_ANCHOR,
	/* 125 */YY_NOT_ACCEPT,
	/* 126 */YY_NO_ANCHOR,
	/* 127 */YY_NOT_ACCEPT,
	/* 128 */YY_NO_ANCHOR,
	/* 129 */YY_NOT_ACCEPT,
	/* 130 */YY_NO_ANCHOR,
	/* 131 */YY_NOT_ACCEPT,
	/* 132 */YY_NO_ANCHOR,
	/* 133 */YY_NOT_ACCEPT,
	/* 134 */YY_NO_ANCHOR,
	/* 135 */YY_NOT_ACCEPT,
	/* 136 */YY_NO_ANCHOR,
	/* 137 */YY_NOT_ACCEPT,
	/* 138 */YY_NO_ANCHOR,
	/* 139 */YY_NOT_ACCEPT,
	/* 140 */YY_NO_ANCHOR,
	/* 141 */YY_NOT_ACCEPT,
	/* 142 */YY_NO_ANCHOR,
	/* 143 */YY_NOT_ACCEPT,
	/* 144 */YY_NO_ANCHOR,
	/* 145 */YY_NOT_ACCEPT,
	/* 146 */YY_NO_ANCHOR,
	/* 147 */YY_NOT_ACCEPT,
	/* 148 */YY_NO_ANCHOR,
	/* 149 */YY_NOT_ACCEPT,
	/* 150 */YY_NO_ANCHOR,
	/* 151 */YY_NOT_ACCEPT,
	/* 152 */YY_NO_ANCHOR,
	/* 153 */YY_NOT_ACCEPT,
	/* 154 */YY_NO_ANCHOR,
	/* 155 */YY_NOT_ACCEPT,
	/* 156 */YY_NO_ANCHOR,
	/* 157 */YY_NOT_ACCEPT,
	/* 158 */YY_NO_ANCHOR,
	/* 159 */YY_NOT_ACCEPT,
	/* 160 */YY_NO_ANCHOR,
	/* 161 */YY_NOT_ACCEPT,
	/* 162 */YY_NO_ANCHOR,
	/* 163 */YY_NOT_ACCEPT,
	/* 164 */YY_NO_ANCHOR,
	/* 165 */YY_NOT_ACCEPT,
	/* 166 */YY_NO_ANCHOR,
	/* 167 */YY_NOT_ACCEPT,
	/* 168 */YY_NO_ANCHOR,
	/* 169 */YY_NOT_ACCEPT,
	/* 170 */YY_NO_ANCHOR,
	/* 171 */YY_NOT_ACCEPT,
	/* 172 */YY_NO_ANCHOR,
	/* 173 */YY_NOT_ACCEPT,
	/* 174 */YY_NO_ANCHOR,
	/* 175 */YY_NOT_ACCEPT,
	/* 176 */YY_NO_ANCHOR,
	/* 177 */YY_NOT_ACCEPT,
	/* 178 */YY_NO_ANCHOR,
	/* 179 */YY_NOT_ACCEPT,
	/* 180 */YY_NO_ANCHOR,
	/* 181 */YY_NOT_ACCEPT,
	/* 182 */YY_NO_ANCHOR,
	/* 183 */YY_NOT_ACCEPT,
	/* 184 */YY_NO_ANCHOR,
	/* 185 */YY_NOT_ACCEPT,
	/* 186 */YY_NO_ANCHOR,
	/* 187 */YY_NOT_ACCEPT,
	/* 188 */YY_NO_ANCHOR,
	/* 189 */YY_NOT_ACCEPT,
	/* 190 */YY_NO_ANCHOR,
	/* 191 */YY_NOT_ACCEPT,
	/* 192 */YY_NO_ANCHOR,
	/* 193 */YY_NOT_ACCEPT,
	/* 194 */YY_NO_ANCHOR,
	/* 195 */YY_NOT_ACCEPT,
	/* 196 */YY_NO_ANCHOR,
	/* 197 */YY_NOT_ACCEPT,
	/* 198 */YY_NO_ANCHOR,
	/* 199 */YY_NOT_ACCEPT,
	/* 200 */YY_NO_ANCHOR,
	/* 201 */YY_NOT_ACCEPT,
	/* 202 */YY_NO_ANCHOR,
	/* 203 */YY_NOT_ACCEPT,
	/* 204 */YY_NO_ANCHOR,
	/* 205 */YY_NOT_ACCEPT,
	/* 206 */YY_NO_ANCHOR,
	/* 207 */YY_NOT_ACCEPT,
	/* 208 */YY_NO_ANCHOR,
	/* 209 */YY_NOT_ACCEPT,
	/* 210 */YY_NO_ANCHOR,
	/* 211 */YY_NOT_ACCEPT,
	/* 212 */YY_NO_ANCHOR,
	/* 213 */YY_NOT_ACCEPT,
	/* 214 */YY_NO_ANCHOR,
	/* 215 */YY_NOT_ACCEPT,
	/* 216 */YY_NO_ANCHOR,
	/* 217 */YY_NOT_ACCEPT,
	/* 218 */YY_NO_ANCHOR,
	/* 219 */YY_NOT_ACCEPT,
	/* 220 */YY_NO_ANCHOR,
	/* 221 */YY_NOT_ACCEPT,
	/* 222 */YY_NO_ANCHOR,
	/* 223 */YY_NOT_ACCEPT,
	/* 224 */YY_NO_ANCHOR,
	/* 225 */YY_NOT_ACCEPT,
	/* 226 */YY_NO_ANCHOR,
	/* 227 */YY_NOT_ACCEPT,
	/* 228 */YY_NO_ANCHOR,
	/* 229 */YY_NOT_ACCEPT,
	/* 230 */YY_NO_ANCHOR,
	/* 231 */YY_NOT_ACCEPT,
	/* 232 */YY_NO_ANCHOR,
	/* 233 */YY_NOT_ACCEPT,
	/* 234 */YY_NO_ANCHOR,
	/* 235 */YY_NOT_ACCEPT,
	/* 236 */YY_NO_ANCHOR,
	/* 237 */YY_NOT_ACCEPT,
	/* 238 */YY_NO_ANCHOR,
	/* 239 */YY_NOT_ACCEPT,
	/* 240 */YY_NO_ANCHOR,
	/* 241 */YY_NOT_ACCEPT,
	/* 242 */YY_NO_ANCHOR,
	/* 243 */YY_NOT_ACCEPT,
	/* 244 */YY_NO_ANCHOR,
	/* 245 */YY_NOT_ACCEPT,
	/* 246 */YY_NO_ANCHOR,
	/* 247 */YY_NOT_ACCEPT,
	/* 248 */YY_NO_ANCHOR,
	/* 249 */YY_NOT_ACCEPT,
	/* 250 */YY_NO_ANCHOR,
	/* 251 */YY_NOT_ACCEPT,
	/* 252 */YY_NO_ANCHOR,
	/* 253 */YY_NOT_ACCEPT,
	/* 254 */YY_NO_ANCHOR,
	/* 255 */YY_NO_ANCHOR,
	/* 256 */YY_NO_ANCHOR,
	/* 257 */YY_NO_ANCHOR,
	/* 258 */YY_NO_ANCHOR,
	/* 259 */YY_NO_ANCHOR,
	/* 260 */YY_NO_ANCHOR,
	/* 261 */YY_NO_ANCHOR,
	/* 262 */YY_NO_ANCHOR,
	/* 263 */YY_NO_ANCHOR,
	/* 264 */YY_NO_ANCHOR,
	/* 265 */YY_NO_ANCHOR,
	/* 266 */YY_NO_ANCHOR,
	/* 267 */YY_NO_ANCHOR,
	/* 268 */YY_NO_ANCHOR,
	/* 269 */YY_NO_ANCHOR,
	/* 270 */YY_NO_ANCHOR,
	/* 271 */YY_NO_ANCHOR,
	/* 272 */YY_NO_ANCHOR,
	/* 273 */YY_NO_ANCHOR,
	/* 274 */YY_NO_ANCHOR,
	/* 275 */YY_NO_ANCHOR,
	/* 276 */YY_NO_ANCHOR,
	/* 277 */YY_NO_ANCHOR,
	/* 278 */YY_NO_ANCHOR,
	/* 279 */YY_NO_ANCHOR,
	/* 280 */YY_NOT_ACCEPT,
	/* 281 */YY_NO_ANCHOR,
	/* 282 */YY_NOT_ACCEPT,
	/* 283 */YY_NOT_ACCEPT,
	/* 284 */YY_NOT_ACCEPT,
	/* 285 */YY_NO_ANCHOR,
	/* 286 */YY_NOT_ACCEPT,
	/* 287 */YY_NO_ANCHOR,
	/* 288 */YY_NOT_ACCEPT,
	/* 289 */YY_NO_ANCHOR,
	/* 290 */YY_NOT_ACCEPT,
	/* 291 */YY_NO_ANCHOR,
	/* 292 */YY_NOT_ACCEPT,
	/* 293 */YY_NO_ANCHOR,
	/* 294 */YY_NO_ANCHOR,
	/* 295 */YY_NOT_ACCEPT,
	/* 296 */YY_NOT_ACCEPT,
	/* 297 */YY_NO_ANCHOR,
	/* 298 */YY_NOT_ACCEPT,
	/* 299 */YY_NO_ANCHOR,
	/* 300 */YY_NO_ANCHOR,
	/* 301 */YY_NO_ANCHOR,
	/* 302 */YY_NO_ANCHOR,
	/* 303 */YY_NOT_ACCEPT,
	/* 304 */YY_NOT_ACCEPT,
	/* 305 */YY_NOT_ACCEPT,
	/* 306 */YY_NOT_ACCEPT,
	/* 307 */YY_NO_ANCHOR,
	/* 308 */YY_NO_ANCHOR,
	/* 309 */YY_NO_ANCHOR,
	/* 310 */YY_NO_ANCHOR,
	/* 311 */YY_NOT_ACCEPT,
	/* 312 */YY_NOT_ACCEPT,
	/* 313 */YY_NO_ANCHOR,
	/* 314 */YY_NOT_ACCEPT,
	/* 315 */YY_NO_ANCHOR,
	/* 316 */YY_NOT_ACCEPT,
	/* 317 */YY_NO_ANCHOR,
	/* 318 */YY_NO_ANCHOR,
	/* 319 */YY_NO_ANCHOR,
	/* 320 */YY_NO_ANCHOR,
	/* 321 */YY_NO_ANCHOR,
	/* 322 */YY_NO_ANCHOR,
	/* 323 */YY_NO_ANCHOR,
	/* 324 */YY_NOT_ACCEPT,
	/* 325 */YY_NOT_ACCEPT,
	/* 326 */YY_NOT_ACCEPT,
	/* 327 */YY_NO_ANCHOR,
	/* 328 */YY_NO_ANCHOR,
	/* 329 */YY_NO_ANCHOR,
	/* 330 */YY_NO_ANCHOR,
	/* 331 */YY_NO_ANCHOR,
	/* 332 */YY_NOT_ACCEPT,
	/* 333 */YY_NOT_ACCEPT,
	/* 334 */YY_NOT_ACCEPT,
	/* 335 */YY_NO_ANCHOR,
	/* 336 */YY_NO_ANCHOR,
	/* 337 */YY_NO_ANCHOR,
	/* 338 */YY_NO_ANCHOR,
	/* 339 */YY_NO_ANCHOR,
	/* 340 */YY_NOT_ACCEPT,
	/* 341 */YY_NO_ANCHOR,
	/* 342 */YY_NO_ANCHOR,
	/* 343 */YY_NO_ANCHOR,
	/* 344 */YY_NOT_ACCEPT,
	/* 345 */YY_NO_ANCHOR,
	/* 346 */YY_NO_ANCHOR,
	/* 347 */YY_NO_ANCHOR,
	/* 348 */YY_NO_ANCHOR,
	/* 349 */YY_NOT_ACCEPT,
	/* 350 */YY_NO_ANCHOR,
	/* 351 */YY_NO_ANCHOR,
	/* 352 */YY_NO_ANCHOR,
	/* 353 */YY_NO_ANCHOR,
	/* 354 */YY_NO_ANCHOR,
	/* 355 */YY_NOT_ACCEPT,
	/* 356 */YY_NO_ANCHOR,
	/* 357 */YY_NO_ANCHOR,
	/* 358 */YY_NO_ANCHOR,
	/* 359 */YY_NO_ANCHOR,
	/* 360 */YY_NO_ANCHOR,
	/* 361 */YY_NO_ANCHOR,
	/* 362 */YY_NO_ANCHOR,
	/* 363 */YY_NO_ANCHOR,
	/* 364 */YY_NO_ANCHOR,
	/* 365 */YY_NO_ANCHOR,
	/* 366 */YY_NO_ANCHOR,
	/* 367 */YY_NO_ANCHOR,
	/* 368 */YY_NO_ANCHOR,
	/* 369 */YY_NO_ANCHOR,
	/* 370 */YY_NO_ANCHOR,
	/* 371 */YY_NO_ANCHOR,
	/* 372 */YY_NO_ANCHOR,
	/* 373 */YY_NO_ANCHOR,
	/* 374 */YY_NO_ANCHOR,
	/* 375 */YY_NO_ANCHOR,
	/* 376 */YY_NOT_ACCEPT,
	/* 377 */YY_NOT_ACCEPT,
	/* 378 */YY_NOT_ACCEPT,
	/* 379 */YY_NOT_ACCEPT,
	/* 380 */YY_NOT_ACCEPT,
	/* 381 */YY_NOT_ACCEPT,
	/* 382 */YY_NOT_ACCEPT,
	/* 383 */YY_NOT_ACCEPT };
	private int yy_cmap[] = unpackFromString(
			1,
			130,
			"44:8,1:3,44,1:2,44:18,1,28,44:4,38,44,42,43,44:2,37,25,34,44,33,30,31,32,33"
					+ ":6,41,44,9,10,11,36,44,35:32,4,29,15,6,14,3,26,27,13,35,22,24,21,5,7,18,35,"
					+ "8,20,23,16,19,2,17,35:2,39,44,40,12,44,0:2")[0];

	private int yy_rmap[] = unpackFromString(
			1,
			384,
			"0,1,2,3,4,1:3,5,1:8,6,1,6,7,8,6,1:4,6:8,9,6:15,10,6:3,1,6:2,1:8,6,1:11,11,1"
					+ "2,13,14,15,16,1,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36"
					+ ",37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61"
					+ ",62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86"
					+ ",87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108"
					+ ",109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,12"
					+ "7,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,1"
					+ "46,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,"
					+ "165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183"
					+ ",184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,20"
					+ "2,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,2"
					+ "21,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,"
					+ "240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258"
					+ ",259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,27"
					+ "7,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,2"
					+ "96,297,298,299,300,301,6,302,303,304,305,306,307,308,309,310,311,312,313,31"
					+ "4")[0];

	private int yy_nxt[][] = unpackFromString(
			315,
			45,
			"1,2,3,276,330,353,362,79,365,4,80,84,5,366,367,368,369,370,371,83,372,370,3"
					+ "73,374,375,6,370:2,7,370,8:4,9,370,10,11,277,12,13,14,15,16,84,-1:46,2,-1:4"
					+ "5,370,86,370:5,-1:4,88,370:11,-1,370:2,-1,370,-1:5,370,-1:19,78,-1:44,85,-1"
					+ ":19,8:4,87,-1:12,370:7,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:5,172"
					+ ",370,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,176,370:6,-1:4,302,370:11,-"
					+ "1,370:2,-1,370,-1:5,370,-1:11,370:3,232,370:3,-1:4,370:12,-1,370:2,-1,370,-"
					+ "1:5,370,-1:11,370:7,-1:4,370:12,-1,370:2,55,370,-1:5,370,-1:20,23,-1:35,370"
					+ ":6,17,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:20,18,-1:43,85,-1:19,81:4,-1:"
					+ "22,24,-1:35,370:7,-1,82,-1:2,370:12,-1,370:2,-1,370,-1:5,370,-1:20,25,-1:35"
					+ ",370,19,370:5,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:39,81:4,-1:13,370:7,-"
					+ "1:4,370:10,130,370,-1,370:2,-1,370,-1:5,370,-1:20,26,-1:35,370:7,-1:4,370:1"
					+ "1,293,-1,370:2,-1,370,-1:5,370,-1:32,95,-1:23,370:4,20,370:2,-1:4,370:12,-1"
					+ ",370:2,-1,370,-1:5,370,-1:15,97,-1:8,280,-1:5,332,-1:25,370:4,132,370:2,-1:"
					+ "4,370:12,-1,370:2,-1,370,-1:5,370,-1:16,101,-1:39,370:7,-1:4,370:9,21,370:2"
					+ ",-1,370:2,-1,370,-1:5,370,-1:23,105,-1:32,370:7,-1:4,370:11,22,-1,370:2,-1,"
					+ "370,-1:5,370,-1:11,282,-1,111,-1:10,113,-1:7,115,-1:23,370:3,136,370:2,281,"
					+ "-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:34,117,-1:21,370:7,-1:4,370:4,294,3"
					+ "70:7,-1,370:2,-1,370,-1:5,370,-1:22,283,-1:33,370:7,-1:4,370:8,291,370:3,-1"
					+ ",370:2,-1,370,-1:5,370,-1:12,123,-1:43,370,339,370:5,-1:4,370:7,138,140,370"
					+ ":3,-1,370:2,-1,370,-1:5,370,-1:14,125,-1:41,370:4,142,370:2,-1:4,370:8,289,"
					+ "370:3,-1,370:2,-1,370,-1:5,370,-1:15,286,-1:40,370,287,370:5,-1:4,370:12,-1"
					+ ",370:2,-1,370,-1:5,370,-1:29,284,-1:26,370:6,336,-1:4,370:12,-1,370:2,-1,37"
					+ "0,-1:5,370,-1:16,129,-1:39,370:7,-1:4,370:5,144,370:6,-1,370:2,-1,370,-1:5,"
					+ "370,-1:23,288,-1:32,370:3,146,370:3,-1:4,370:8,148,370:3,-1,370:2,-1,370,-1"
					+ ":5,370,-1:24,131,-1:31,370:7,-1:4,370,337,370:10,-1,370:2,-1,370,-1:5,370,-"
					+ "1:17,133,-1:38,370:4,150,370:2,-1:4,370:10,152,357,-1,370:2,-1,370,-1:5,370"
					+ ",-1:12,135,-1:14,290,-1:28,370:5,158,370,-1:4,370:12,-1,370:2,-1,370,-1:5,3"
					+ "70,-1:13,137,-1:42,370:2,160,370:4,-1:4,370:3,162,370:8,-1,370:2,-1,370,-1:"
					+ "5,370,-1:32,139,-1:23,370:2,164,370:4,-1:4,370:12,-1,370:2,-1,370,-1:5,370,"
					+ "-1:12,143,-1:43,370:7,-1:4,370:7,166,370:4,-1,370:2,-1,370,-1:5,370,-1:14,1"
					+ "45,-1:41,370:7,-1:4,370:12,-1,370,168,-1,370,-1:5,370,-1:16,149,-1:39,370:7"
					+ ",-1:4,370:12,91,370:2,-1,370,-1:5,370,-1:23,334,-1:32,370:7,-1:4,174,370:11"
					+ ",-1,370:2,-1,370,-1:5,370,-1:17,151,-1:38,370:7,-1:4,370,27,370:10,-1,370:2"
					+ ",-1,370,-1:5,370,-1:25,159,-1:30,370:7,-1:4,370:2,301,370:9,-1,370:2,-1,370"
					+ ",-1:5,370,-1:23,161,-1:32,370:5,28,370,-1:4,370:12,-1,370:2,-1,370,-1:5,370"
					+ ",-1:34,163,-1:21,370:7,-1:4,370:3,186,370:8,-1,370:2,-1,370,-1:5,370,-1:29,"
					+ "58,-1:26,370:2,310,370:4,-1:4,370,358,370:10,-1,370:2,-1,370,-1:5,370,-1:32"
					+ ",167,-1:23,370:7,-1:4,370:6,341,370:5,-1,370:2,-1,370,-1:5,370,-1:30,169,-1"
					+ ":25,370:7,-1:4,370:5,188,370:6,-1,370:2,-1,370,-1:5,370,-1:14,171,-1:41,370"
					+ ":7,-1:4,370,359,370:10,-1,370:2,-1,370,-1:5,370,-1:13,173,-1:42,370:6,307,-"
					+ "1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:24,303,-1:7,304,-1:23,370,313,370:5,"
					+ "-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:16,306,-1:39,370:7,-1:4,370:12,93,3"
					+ "70:2,-1,370,-1:5,370,-1:27,179,-1:28,29,370:6,-1:4,370:12,-1,370:2,-1,370,-"
					+ "1:5,370,-1:33,344,-1:22,370:3,308,370:3,-1:4,370:2,196,370:9,-1,370:2,-1,37"
					+ "0,-1:5,370,-1:26,181,-1:29,370:7,-1:4,370,30,370:10,-1,370:2,-1,370,-1:5,37"
					+ "0,-1:39,59,60,61,-1:14,370:4,31,370:2,-1:4,370:12,-1,370:2,-1,370,-1:5,370,"
					+ "-1:17,377,-1:38,370:7,-1:4,370:5,32,370:4,198,370,-1,370:2,-1,370,-1:5,370,"
					+ "-1:23,183,-1:32,370:2,200,370:4,-1:4,370:7,348,370:4,-1,370:2,-1,370,-1:5,3"
					+ "70,-1:29,62,-1:26,370:7,-1:4,370,33,370:10,-1,370:2,-1,370,-1:5,370,-1:32,1"
					+ "85,-1:23,370:6,34,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:30,189,-1:25,370:"
					+ "3,202,370:3,-1:4,370:6,345,370:5,-1,370:2,-1,370,-1:5,370,-1:36,63,-1:19,37"
					+ "0:7,-1:4,370:12,-1,370,35,-1,370,-1:5,370,-1:17,64,-1:38,370:2,204,370:4,-1"
					+ ":4,370:12,-1,370:2,-1,370,-1:5,370,-1:13,195,-1:42,370:7,-1:4,370:7,206,370"
					+ ":4,-1,370:2,-1,370,-1:5,370,-1:32,65,-1:23,370:7,-1:4,360,370:11,-1,370:2,-"
					+ "1,370,-1:5,370,-1:26,314,-1:29,370:3,208,370:3,-1:4,370:12,-1,370:2,-1,370,"
					+ "-1:5,370,-1:23,197,-1:32,370:7,-1:4,370:2,136,370:9,-1,370:2,-1,370,-1:5,37"
					+ "0,-1:17,199,-1:38,370:5,218,370,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:23,"
					+ "67,-1:32,370:7,-1:4,370:12,-1,370:2,-1,361,-1:5,370,-1:14,349,-1:41,370:7,-"
					+ "1:4,370:7,36,370:4,-1,370:2,-1,370,-1:5,370,-1:34,203,-1:21,370:6,37,-1:4,3"
					+ "70:12,-1,370:2,-1,370,-1:5,370,-1:32,207,-1:23,370:7,-1:4,370,38,370:10,-1,"
					+ "370:2,-1,370,-1:5,370,-1:26,213,-1:29,370:7,-1:4,370:12,99,370:2,-1,370,-1:"
					+ "5,370,-1:22,316,-1:33,370:7,-1:4,370:11,228,-1,370:2,-1,370,-1:5,370,-1:30,"
					+ "217,-1:25,370:7,-1:4,370:12,-1,39,370,-1,370,-1:5,370,-1:24,219,-1:31,370:7"
					+ ",-1:4,370:11,40,-1,370:2,-1,370,-1:5,370,-1:17,376,-1:38,370:7,-1:4,370:10,"
					+ "234,370,-1,370:2,-1,370,-1:5,370,-1:36,68,-1:19,370:7,-1:4,370,238,370:10,-"
					+ "1,370:2,-1,370,-1:5,370,-1:15,221,-1:40,370:7,-1:4,240,370:11,-1,370:2,-1,3"
					+ "70,-1:5,370,-1:29,69,-1:26,370:4,27,370:2,-1:4,370:12,-1,370:2,-1,370,-1:5,"
					+ "370,-1:32,70,-1:23,370:7,-1:4,370:10,41,370,-1,370:2,-1,370,-1:5,370,-1:23,"
					+ "225,-1:32,370:6,321,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:29,71,-1:26,370"
					+ ":7,-1:4,370:7,246,370:4,-1,370:2,-1,370,-1:5,370,-1:16,227,-1:39,370:7,-1:4"
					+ ",370:9,248,370:2,103,370:2,-1,370,-1:5,370,-1:34,229,-1:21,370:7,-1:4,370:2"
					+ ",250,370:9,-1,370:2,-1,370,-1:5,370,-1:32,72,-1:23,370:3,254,370:3,-1:4,370"
					+ ":12,-1,370:2,-1,370,-1:5,370,-1:26,231,-1:29,370:7,-1:4,370:12,-1,370,42,-1"
					+ ",370,-1:5,370,-1:14,233,-1:41,370:7,-1:4,370:11,43,-1,370:2,-1,370,-1:5,370"
					+ ",-1:11,235,-1:44,370:7,-1:4,370:8,257,370:3,-1,370:2,-1,370,-1:5,370,-1:32,"
					+ "73,-1:23,370:5,259,370,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:32,237,-1:23"
					+ ",370:7,-1:4,370:7,44,370:4,-1,370:2,-1,370,-1:5,370,-1:12,325,-1:43,370:7,-"
					+ "1:4,370:12,119,370:2,-1,370,-1:5,370,-1:23,241,-1:32,370:7,-1:4,370:12,121,"
					+ "370:2,-1,370,-1:5,370,-1:17,243,-1:38,370:7,-1:4,370:12,-1,370:2,-1,327,-1:"
					+ "5,370,-1:26,247,-1:29,370:7,-1:4,370:12,333,370:2,-1,370,-1:5,370,-1:22,249"
					+ ",-1:33,370:7,-1:4,370,262,370:10,-1,370:2,-1,370,-1:5,370,-1:29,74,-1:26,37"
					+ "0:7,-1:4,370,45,370:10,-1,370:2,-1,370,-1:5,370,-1:32,75,-1:23,370:7,-1:4,3"
					+ "70:12,-1,370:2,-1,46,-1:5,370,-1:24,253,-1:31,370:7,-1:4,370,47,370:10,-1,3"
					+ "70:2,-1,370,-1:5,370,-1:32,76,-1:23,370:7,-1:4,370:11,328,-1,370:2,-1,370,-"
					+ "1:5,370,-1:32,77,-1:23,370:7,-1:4,370:12,-1,48,370,-1,370,-1:5,370,-1:11,37"
					+ "0:7,-1:4,370:8,49,370:3,-1,370:2,-1,370,-1:5,370,-1:11,370:2,265,370:4,-1:4"
					+ ",370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,50,370:10,-1,370:2,-1"
					+ ",370,-1:5,370,-1:11,370:7,-1:4,370,51,370:10,-1,370:2,-1,370,-1:5,370,-1:11"
					+ ",370:7,-1:4,370:10,52,370,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:10,"
					+ "53,370,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:2,267,370:9,-1,370:2,-"
					+ "1,370,-1:5,370,-1:11,370:7,-1:4,370:4,268,370:7,-1,370:2,-1,370,-1:5,370,-1"
					+ ":11,370:7,-1:4,370,54,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,"
					+ "269,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:10,329,370,-1,370:"
					+ "2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:12,153,370:2,-1,370,-1:5,370,-1:11,3"
					+ "70:7,-1:4,370,56,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,271,370:1"
					+ "1,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:12,157,370:2,-1,370,-1:5,37"
					+ "0,-1:11,370:4,57,370:2,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:"
					+ "4,370:6,273,370:5,-1,370:2,-1,370,-1:5,370,-1:11,370:5,274,370,-1:4,370:12,"
					+ "-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,275,370:10,-1,370:2,-1,370,-1"
					+ ":5,370,-1:11,370:3,66,370:3,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:"
					+ "7,-1:4,370:12,205,370:2,-1,370,-1:5,370,-1:11,370:2,90,370:4,-1:4,370:12,-1"
					+ ",370:2,-1,370,-1:5,370,-1:19,89,-1:36,370:7,-1:4,370:10,134,370,-1,370:2,-1"
					+ ",370,-1:5,370,-1:11,370:7,-1:4,370:11,297,-1,370:2,-1,370,-1:5,370,-1:16,10"
					+ "7,-1:39,370:7,-1:4,370:8,178,370:3,-1,370:2,-1,370,-1:5,370,-1:12,127,-1:46"
					+ ",292,-1:59,296,-1:26,370:6,338,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:23,1"
					+ "41,-1:32,370:7,-1:4,370,342,370:10,-1,370:2,-1,370,-1:5,370,-1:17,147,-1:38"
					+ ",370:5,299,370,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:13,298,-1:42,370:2,1"
					+ "82,370:4,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:12,340,-1:43,370:7,-1:4,37"
					+ "0:7,170,370:4,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,180,370:11,-1,370:2"
					+ ",-1,370,-1:5,370,-1:17,155,-1:50,165,-1:32,370:7,-1:4,370:3,192,370:8,-1,37"
					+ "0:2,-1,370,-1:5,370,-1:32,175,-1:23,370:7,-1:4,370:6,318,370:5,-1,370:2,-1,"
					+ "370,-1:5,370,-1:11,370:7,-1:4,370,346,370:10,-1,370:2,-1,370,-1:5,370,-1:11"
					+ ",370:6,210,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370,347,370:5,-1:4,37"
					+ "0:12,-1,370:2,-1,370,-1:5,370,-1:16,191,-1:51,312,-1:53,187,-1:42,193,-1:25"
					+ ",370:2,222,370:4,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:"
					+ "7,350,370:4,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,224,370:11,-1,370:2,-"
					+ "1,370,-1:5,370,-1:11,370:3,212,370:3,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-"
					+ "1:23,209,-1:38,201,-1:38,370:5,320,370,-1:4,370:12,-1,370:2,-1,370,-1:5,370"
					+ ",-1:32,211,-1:23,370:7,-1:4,370:11,244,-1,370:2,-1,370,-1:5,370,-1:24,223,-"
					+ "1:31,370:7,-1:4,370:10,258,370,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,37"
					+ "0,242,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,352,370:11,-1,370:2,"
					+ "-1,370,-1:5,370,-1:11,370:6,255,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,"
					+ "370:7,-1:4,370:7,263,370:4,-1,370:2,-1,370,-1:5,370,-1:11,370:3,261,370:3,-"
					+ "1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:5,260,370,-1:4,370:12,-1,370:"
					+ "2,-1,370,-1:5,370,-1:32,239,-1:24,245,-1:58,251,-1:29,370:7,-1:4,370,266,37"
					+ "0:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,270,370:10,-1,370:2,-1,3"
					+ "70,-1:5,370,-1:11,370:7,-1:4,272,370:11,-1,370:2,-1,370,-1:5,370,-1:11,370:"
					+ "3,92,94,370:2,-1:4,370:2,278,370:4,96,370:3,98,-1,370:2,-1,370,-1:5,370,-1:"
					+ "11,370:7,-1:4,370:10,156,370,-1,370:2,-1,370,-1:5,370,-1:16,109,-1:40,295,-"
					+ "1:61,305,-1:26,370:6,154,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-"
					+ "1:4,370,343,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:2,364,370:4,-1:4,370:"
					+ "12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:7,309,370:4,-1,370:2,-1,37"
					+ "0,-1:5,370,-1:11,370:7,-1:4,184,370:11,-1,370:2,-1,370,-1:5,370,-1:23,177,-"
					+ "1:32,370:7,-1:4,370,216,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:6,351,-1:"
					+ "4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370,315,370:5,-1:4,370:12,-1,370:2,"
					+ "-1,370,-1:5,370,-1:32,383,-1:23,370:2,317,370:4,-1:4,370:12,-1,370:2,-1,370"
					+ ",-1:5,370,-1:11,370:7,-1:4,370:7,226,370:4,-1,370:2,-1,370,-1:5,370,-1:11,3"
					+ "70:3,323,370:3,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:5,230,370,-1:"
					+ "4,370:12,-1,370:2,-1,370,-1:5,370,-1:32,215,-1:23,370:7,-1:4,370:11,256,-1,"
					+ "370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,322,370:10,-1,370:2,-1,370,-1:5,"
					+ "370,-1:11,370:3,264,370:3,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:5,"
					+ "100,370,-1:4,370,102,370:10,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370:1"
					+ "0,363,370,-1,370:2,-1,370,-1:5,370,-1:29,324,-1:26,370:6,300,-1:4,370:12,-1"
					+ ",370:2,-1,370,-1:5,370,-1:11,370:2,190,370:4,-1:4,370:12,-1,370:2,-1,370,-1"
					+ ":5,370,-1:11,370:6,214,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370,319,3"
					+ "70:5,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:3,236,370:3,-1:4,370:12"
					+ ",-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,252,370:10,-1,370:2,-1,370,-"
					+ "1:5,370,-1:11,370:5,104,370,-1:4,370,106,370:10,-1,370:2,-1,370,-1:5,370,-1"
					+ ":11,370:2,194,370:4,-1:4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:6,220,-1"
					+ ":4,370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,108,370:10,-1,370:2"
					+ ",-1,370,-1:5,370,-1:11,370:3,110,370:2,112,-1:4,370:12,-1,370:2,-1,370,-1:5"
					+ ",370,-1:11,370:7,-1:4,370:4,114,370:7,-1,370:2,-1,370,-1:5,370,-1:11,370:5,"
					+ "116,370,-1:4,370:11,118,-1,370:2,-1,370,-1:5,370,-1:11,370:3,120,370:3,-1:4"
					+ ",370:12,-1,370:2,-1,370,-1:5,370,-1:11,370:2,285,370:4,-1:4,370,335,370:10,"
					+ "-1,370:2,-1,370,-1:5,370,-1:11,370:7,-1:4,370,331,370:10,-1,370,122,-1,370,"
					+ "-1:5,370,-1:11,370:7,-1:4,370:5,279,370,354,370:4,-1,370:2,-1,370,-1:5,370,"
					+ "-1:11,370:6,124,-1:4,370:12,-1,370,356,-1,370,-1:5,370,-1:11,370:5,126,370,"
					+ "-1:4,128,370:11,-1,370:2,-1,370,-1:5,370,-1:23,355,-1:53,311,-1:35,326,-1:5"
					+ "3,378,-1:26,379,-1:46,380,-1:52,381,-1:54,382,-1:19");

	public java_cup.runtime.Symbol next_token() throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol)
				yy_lookahead = YY_BOL;
			else
				yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

				return new Symbol(sym.EOF, null);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			} else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				} else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:

					case -2:
						break;
					case 2: {
					}
					case -3:
						break;
					case 3: {
						return new Symbol(sym.ID, yytext());
					}
					case -4:
						break;
					case 4: {
						return new Symbol(sym.error, "Error!");
					}
					case -5:
						break;
					case 5: {
						return new Symbol(sym.NOT, yytext());
					}
					case -6:
						break;
					case 6: {
						return new Symbol(sym.DASH, yytext());
					}
					case -7:
						break;
					case 7: {
						return new Symbol(sym.EM, yytext());
					}
					case -8:
						break;
					case 8: {
						return new Symbol(sym.NM, ""
								+ Integer.parseInt(yytext()));
					}
					case -9:
						break;
					case 9: {
						return new Symbol(sym.DT, yytext());
					}
					case -10:
						break;
					case 10: {
						return new Symbol(sym.QM, yytext());
					}
					case -11:
						break;
					case 11: {
						return new Symbol(sym.COMA, yytext());
					}
					case -12:
						break;
					case 12: {
						return new Symbol(sym.RCURLY, yytext());
					}
					case -13:
						break;
					case 13: {
						return new Symbol(sym.LCURLY, yytext());
					}
					case -14:
						break;
					case 14: {
						return new Symbol(sym.CL, yytext());
					}
					case -15:
						break;
					case 15: {
						return new Symbol(sym.LP, yytext());
					}
					case -16:
						break;
					case 16: {
						return new Symbol(sym.RP, yytext());
					}
					case -17:
						break;
					case 17: {
						return new Symbol(sym.OR, yytext());
					}
					case -18:
						break;
					case 18: {
						return new Symbol(sym.ARROW, yytext());
					}
					case -19:
						break;
					case 19: {
						return new Symbol(sym.WF, yytext());
					}
					case -20:
						break;
					case 20: {
						return new Symbol(sym.AND, yytext());
					}
					case -21:
						break;
					case 21: {
						return new Symbol(sym.ASK, yytext());
					}
					case -22:
						break;
					case 22: {
						return new Symbol(sym.ALL, yytext());
					}
					case -23:
						break;
					case 23: {
						return new Symbol(sym.DOUBLEIMPLICATION, yytext());
					}
					case -24:
						break;
					case 24: {
						return new Symbol(sym.ORENTAIL, yytext());
					}
					case -25:
						break;
					case 25: {
						return new Symbol(sym.II, yytext());
					}
					case -26:
						break;
					case 26: {
						return new Symbol(sym.ANDENTAIL, yytext());
					}
					case -27:
						break;
					case 27: {
						return new Symbol(sym.ADJUST, yytext());
					}
					case -28:
						break;
					case 28: {
						return new Symbol(sym.DEMO, yytext());
					}
					case -29:
						break;
					case 29: {
						return new Symbol(sym.SHOW, yytext());
					}
					case -30:
						break;
					// case 30: {
					// return new Symbol(sym.TRUE, yytext());
					// }
					case -31:
						break;
					case 31: {
						return new Symbol(sym.LOAD, yytext());
					}
					case -32:
						break;
					case 32: {
						return new Symbol(sym.LISP, yytext());
					}
					case -33:
						break;
//					case 33: {
//						return new Symbol(sym.FALSE, yytext());
//					}
					case -34:
						break;
					case 34: {
						return new Symbol(sym.ANDOR, yytext());
					}
					case -35:
						break;
					case 35: {
						return new Symbol(sym.ASKWH, yytext());
					}
					case -36:
						break;
					case 36: {
						return new Symbol(sym.KPLUS, yytext());
					}
					case -37:
						break;
					case 37: {
						return new Symbol(sym.KSTAR, yytext());
					}
					case -38:
						break;
					case 38: {
						return new Symbol(sym.TRACE, yytext());
					}
					case -39:
						break;
					case 39: {
						return new Symbol(sym.ACTING, yytext());
					}
					case -40:
						break;
					case 40: {
						return new Symbol(sym.NORMAL, yytext());
					}
					case -41:
						break;
					case 41: {
						return new Symbol(sym.EXPERT, yytext());
					}
					case -42:
						break;
					case 42: {
						return new Symbol(sym.THRESH, yytext());
					}
					case -43:
						break;
					case 43: {
						return new Symbol(sym.WITHALL, yytext());
					}
					case -44:
						break;
					case 44: {
						return new Symbol(sym.NEXISTS, yytext());
					}
					case -45:
						break;
					case 45: {
						return new Symbol(sym.COMPOSE, yytext());
					}
					case -46:
						break;
					case 46: {
						return new Symbol(sym.CLEARKB, yytext());
					}
					case -47:
						break;
					case 47: {
						return new Symbol(sym.UNTRACE, yytext());
					}
					case -48:
						break;
					case 48: {
						return new Symbol(sym.PARSING, yytext());
					}
					case -49:
						break;
					case 49: {
						return new Symbol(sym.PERFORM, yytext());
					}
					case -50:
						break;
					case 50: {
						return new Symbol(sym.WITHSOME, yytext());
					}
					case -51:
						break;
					case 51: {
						return new Symbol(sym.ACTIVATE, yytext());
					}
					case -52:
						break;
					case 52: {
						return new Symbol(sym.ASKWHNOT, yytext());
					}
					case -53:
						break;
					case 53: {
						return new Symbol(sym.ASKIFNOT, yytext());
					}
					case -54:
						break;
					case 54: {
						return new Symbol(sym.CONVERSE, yytext());
					}
					case -55:
						break;
					case 55: {
						return new Symbol(sym.ACTIVATEASSERT, yytext());
					}
					case -56:
						break;
					case 56: {
						return new Symbol(sym.INFERENCE, yytext());
					}
					case -57:
						break;
					case 57: {
						return new Symbol(sym.UNLABELED, yytext());
					}
					case -58:
						break;
					case 58: {
						return new Symbol(sym.LISTWFFS, yytext());
					}
					case -59:
						break;
					case 59: {
						return new Symbol(sym.SETMODE1, yytext());
					}
					case -60:
						break;
					case 60: {
						return new Symbol(sym.SETMODE2, yytext());
					}
					case -61:
						break;
					case 61: {
						return new Symbol(sym.SETMODE3, yytext());
					}
					case -62:
						break;
					case 62: {
						return new Symbol(sym.LISTTERMS, yytext());
					}
					case -63:
						break;
					case 63: {
						return new Symbol(sym.DEFINEPATH, yytext());
					}
					case -64:
						break;
					case 64: {
						return new Symbol(sym.CLEARINFER, yytext());
					}
					case -65:
						break;
					case 65: {
						return new Symbol(sym.SETCONTEXT, yytext());
					}
					case -66:
						break;
					case 66: {
						return new Symbol(sym.TRANSLATION, yytext());
					}
					case -67:
						break;
					case 67: {
						return new Symbol(sym.DEFINEFRAME, yytext());
					}
					case -68:
						break;
					case 68: {
						return new Symbol(sym.UNDEFINEPATH, yytext());
					}
					case -69:
						break;
					case 69: {
						return new Symbol(sym.LISTCONTEXTS, yytext());
					}
					case -70:
						break;
					case 70: {
						return new Symbol(sym.ADDTOCONTEXT, yytext());
					}
					case -71:
						break;
					case 71: {
						return new Symbol(sym.DESCRIBETERMS, yytext());
					}
					case -72:
						break;
					case 72: {
						return new Symbol(sym.DOMAINRESTRICT, yytext());
					}
					case -73:
						break;
					case 73: {
						return new Symbol(sym.DESCRIBECONTEXT, yytext());
					}
					case -74:
						break;
					case 74: {
						return new Symbol(sym.LISTASSERTEDWFFS, yytext());
					}
					case -75:
						break;
					case 75: {
						return new Symbol(sym.REMOVEFROMCONTEXT, yytext());
					}
					case -76:
						break;
					case 76: {
						return new Symbol(sym.SETDEFAULTCONTEXT, yytext());
					}
					case -77:
						break;
					case 77: {
						return new Symbol(sym.IRR, yytext());
					}
					case -78:
						break;
					case 79: {
						return new Symbol(sym.ID, yytext());
					}
					case -79:
						break;
					case 80: {
						return new Symbol(sym.error, "Error!");
					}
					case -80:
						break;
					case 81: {
						return new Symbol(sym.NM, ""
								+ Integer.parseInt(yytext()));
					}
					case -81:
						break;
					case 83: {
						return new Symbol(sym.ID, yytext());
					}
					case -82:
						break;
					case 84: {
						return new Symbol(sym.error, "Error!");
					}
					case -83:
						break;
					case 86: {
						return new Symbol(sym.ID, yytext());
					}
					case -84:
						break;
					case 88: {
						return new Symbol(sym.ID, yytext());
					}
					case -85:
						break;
					case 90: {
						return new Symbol(sym.ID, yytext());
					}
					case -86:
						break;
					case 92: {
						return new Symbol(sym.ID, yytext());
					}
					case -87:
						break;
					case 94: {
						return new Symbol(sym.ID, yytext());
					}
					case -88:
						break;
					case 96: {
						return new Symbol(sym.ID, yytext());
					}
					case -89:
						break;
					case 98: {
						return new Symbol(sym.ID, yytext());
					}
					case -90:
						break;
					case 100: {
						return new Symbol(sym.ID, yytext());
					}
					case -91:
						break;
					case 102: {
						return new Symbol(sym.ID, yytext());
					}
					case -92:
						break;
					case 104: {
						return new Symbol(sym.ID, yytext());
					}
					case -93:
						break;
					case 106: {
						return new Symbol(sym.ID, yytext());
					}
					case -94:
						break;
					case 108: {
						return new Symbol(sym.ID, yytext());
					}
					case -95:
						break;
					case 110: {
						return new Symbol(sym.ID, yytext());
					}
					case -96:
						break;
					case 112: {
						return new Symbol(sym.ID, yytext());
					}
					case -97:
						break;
					case 114: {
						return new Symbol(sym.ID, yytext());
					}
					case -98:
						break;
					case 116: {
						return new Symbol(sym.ID, yytext());
					}
					case -99:
						break;
					case 118: {
						return new Symbol(sym.ID, yytext());
					}
					case -100:
						break;
					case 120: {
						return new Symbol(sym.ID, yytext());
					}
					case -101:
						break;
					case 122: {
						return new Symbol(sym.ID, yytext());
					}
					case -102:
						break;
					case 124: {
						return new Symbol(sym.ID, yytext());
					}
					case -103:
						break;
					case 126: {
						return new Symbol(sym.ID, yytext());
					}
					case -104:
						break;
					case 128: {
						return new Symbol(sym.ID, yytext());
					}
					case -105:
						break;
					case 130: {
						return new Symbol(sym.ID, yytext());
					}
					case -106:
						break;
					case 132: {
						return new Symbol(sym.ID, yytext());
					}
					case -107:
						break;
					case 134: {
						return new Symbol(sym.ID, yytext());
					}
					case -108:
						break;
					case 136: {
						return new Symbol(sym.ID, yytext());
					}
					case -109:
						break;
					case 138: {
						return new Symbol(sym.ID, yytext());
					}
					case -110:
						break;
					case 140: {
						return new Symbol(sym.ID, yytext());
					}
					case -111:
						break;
					case 142: {
						return new Symbol(sym.ID, yytext());
					}
					case -112:
						break;
					case 144: {
						return new Symbol(sym.ID, yytext());
					}
					case -113:
						break;
					case 146: {
						return new Symbol(sym.ID, yytext());
					}
					case -114:
						break;
					case 148: {
						return new Symbol(sym.ID, yytext());
					}
					case -115:
						break;
					case 150: {
						return new Symbol(sym.ID, yytext());
					}
					case -116:
						break;
					case 152: {
						return new Symbol(sym.ID, yytext());
					}
					case -117:
						break;
					case 154: {
						return new Symbol(sym.ID, yytext());
					}
					case -118:
						break;
					case 156: {
						return new Symbol(sym.ID, yytext());
					}
					case -119:
						break;
					case 158: {
						return new Symbol(sym.ID, yytext());
					}
					case -120:
						break;
					case 160: {
						return new Symbol(sym.ID, yytext());
					}
					case -121:
						break;
					case 162: {
						return new Symbol(sym.ID, yytext());
					}
					case -122:
						break;
					case 164: {
						return new Symbol(sym.ID, yytext());
					}
					case -123:
						break;
					case 166: {
						return new Symbol(sym.ID, yytext());
					}
					case -124:
						break;
					case 168: {
						return new Symbol(sym.ID, yytext());
					}
					case -125:
						break;
					case 170: {
						return new Symbol(sym.ID, yytext());
					}
					case -126:
						break;
					case 172: {
						return new Symbol(sym.ID, yytext());
					}
					case -127:
						break;
					case 174: {
						return new Symbol(sym.ID, yytext());
					}
					case -128:
						break;
					case 176: {
						return new Symbol(sym.ID, yytext());
					}
					case -129:
						break;
					case 178: {
						return new Symbol(sym.ID, yytext());
					}
					case -130:
						break;
					case 180: {
						return new Symbol(sym.ID, yytext());
					}
					case -131:
						break;
					case 182: {
						return new Symbol(sym.ID, yytext());
					}
					case -132:
						break;
					case 184: {
						return new Symbol(sym.ID, yytext());
					}
					case -133:
						break;
					case 186: {
						return new Symbol(sym.ID, yytext());
					}
					case -134:
						break;
					case 188: {
						return new Symbol(sym.ID, yytext());
					}
					case -135:
						break;
					case 190: {
						return new Symbol(sym.ID, yytext());
					}
					case -136:
						break;
					case 192: {
						return new Symbol(sym.ID, yytext());
					}
					case -137:
						break;
					case 194: {
						return new Symbol(sym.ID, yytext());
					}
					case -138:
						break;
					case 196: {
						return new Symbol(sym.ID, yytext());
					}
					case -139:
						break;
					case 198: {
						return new Symbol(sym.ID, yytext());
					}
					case -140:
						break;
					case 200: {
						return new Symbol(sym.ID, yytext());
					}
					case -141:
						break;
					case 202: {
						return new Symbol(sym.ID, yytext());
					}
					case -142:
						break;
					case 204: {
						return new Symbol(sym.ID, yytext());
					}
					case -143:
						break;
					case 206: {
						return new Symbol(sym.ID, yytext());
					}
					case -144:
						break;
					case 208: {
						return new Symbol(sym.ID, yytext());
					}
					case -145:
						break;
					case 210: {
						return new Symbol(sym.ID, yytext());
					}
					case -146:
						break;
					case 212: {
						return new Symbol(sym.ID, yytext());
					}
					case -147:
						break;
					case 214: {
						return new Symbol(sym.ID, yytext());
					}
					case -148:
						break;
					case 216: {
						return new Symbol(sym.ID, yytext());
					}
					case -149:
						break;
					case 218: {
						return new Symbol(sym.ID, yytext());
					}
					case -150:
						break;
					case 220: {
						return new Symbol(sym.ID, yytext());
					}
					case -151:
						break;
					case 222: {
						return new Symbol(sym.ID, yytext());
					}
					case -152:
						break;
					case 224: {
						return new Symbol(sym.ID, yytext());
					}
					case -153:
						break;
					case 226: {
						return new Symbol(sym.ID, yytext());
					}
					case -154:
						break;
					case 228: {
						return new Symbol(sym.ID, yytext());
					}
					case -155:
						break;
					case 230: {
						return new Symbol(sym.ID, yytext());
					}
					case -156:
						break;
					case 232: {
						return new Symbol(sym.ID, yytext());
					}
					case -157:
						break;
					case 234: {
						return new Symbol(sym.ID, yytext());
					}
					case -158:
						break;
					case 236: {
						return new Symbol(sym.ID, yytext());
					}
					case -159:
						break;
					case 238: {
						return new Symbol(sym.ID, yytext());
					}
					case -160:
						break;
					case 240: {
						return new Symbol(sym.ID, yytext());
					}
					case -161:
						break;
					case 242: {
						return new Symbol(sym.ID, yytext());
					}
					case -162:
						break;
					case 244: {
						return new Symbol(sym.ID, yytext());
					}
					case -163:
						break;
					case 246: {
						return new Symbol(sym.ID, yytext());
					}
					case -164:
						break;
					case 248: {
						return new Symbol(sym.ID, yytext());
					}
					case -165:
						break;
					case 250: {
						return new Symbol(sym.ID, yytext());
					}
					case -166:
						break;
					case 252: {
						return new Symbol(sym.ID, yytext());
					}
					case -167:
						break;
					case 254: {
						return new Symbol(sym.ID, yytext());
					}
					case -168:
						break;
					case 255: {
						return new Symbol(sym.ID, yytext());
					}
					case -169:
						break;
					case 256: {
						return new Symbol(sym.ID, yytext());
					}
					case -170:
						break;
					case 257: {
						return new Symbol(sym.ID, yytext());
					}
					case -171:
						break;
					case 258: {
						return new Symbol(sym.ID, yytext());
					}
					case -172:
						break;
					case 259: {
						return new Symbol(sym.ID, yytext());
					}
					case -173:
						break;
					case 260: {
						return new Symbol(sym.ID, yytext());
					}
					case -174:
						break;
					case 261: {
						return new Symbol(sym.ID, yytext());
					}
					case -175:
						break;
					case 262: {
						return new Symbol(sym.ID, yytext());
					}
					case -176:
						break;
					case 263: {
						return new Symbol(sym.ID, yytext());
					}
					case -177:
						break;
					case 264: {
						return new Symbol(sym.ID, yytext());
					}
					case -178:
						break;
					case 265: {
						return new Symbol(sym.ID, yytext());
					}
					case -179:
						break;
					case 266: {
						return new Symbol(sym.ID, yytext());
					}
					case -180:
						break;
					case 267: {
						return new Symbol(sym.ID, yytext());
					}
					case -181:
						break;
					case 268: {
						return new Symbol(sym.ID, yytext());
					}
					case -182:
						break;
					case 269: {
						return new Symbol(sym.ID, yytext());
					}
					case -183:
						break;
					case 270: {
						return new Symbol(sym.ID, yytext());
					}
					case -184:
						break;
					case 271: {
						return new Symbol(sym.ID, yytext());
					}
					case -185:
						break;
					case 272: {
						return new Symbol(sym.ID, yytext());
					}
					case -186:
						break;
					case 273: {
						return new Symbol(sym.ID, yytext());
					}
					case -187:
						break;
					case 274: {
						return new Symbol(sym.ID, yytext());
					}
					case -188:
						break;
					case 275: {
						return new Symbol(sym.ID, yytext());
					}
					case -189:
						break;
					case 276: {
						return new Symbol(sym.ID, yytext());
					}
					case -190:
						break;
					case 277: {
						return new Symbol(sym.error, "Error!");
					}
					case -191:
						break;
					case 278: {
						return new Symbol(sym.ID, yytext());
					}
					case -192:
						break;
					case 279: {
						return new Symbol(sym.ID, yytext());
					}
					case -193:
						break;
					case 281: {
						return new Symbol(sym.ID, yytext());
					}
					case -194:
						break;
					case 285: {
						return new Symbol(sym.ID, yytext());
					}
					case -195:
						break;
					case 287: {
						return new Symbol(sym.ID, yytext());
					}
					case -196:
						break;
					case 289: {
						return new Symbol(sym.ID, yytext());
					}
					case -197:
						break;
					case 291: {
						return new Symbol(sym.ID, yytext());
					}
					case -198:
						break;
					case 293: {
						return new Symbol(sym.ID, yytext());
					}
					case -199:
						break;
					case 294: {
						return new Symbol(sym.ID, yytext());
					}
					case -200:
						break;
					case 297: {
						return new Symbol(sym.ID, yytext());
					}
					case -201:
						break;
					case 299: {
						return new Symbol(sym.ID, yytext());
					}
					case -202:
						break;
					case 300: {
						return new Symbol(sym.ID, yytext());
					}
					case -203:
						break;
					case 301: {
						return new Symbol(sym.ID, yytext());
					}
					case -204:
						break;
					case 302: {
						return new Symbol(sym.ID, yytext());
					}
					case -205:
						break;
					case 307: {
						return new Symbol(sym.ID, yytext());
					}
					case -206:
						break;
					case 308: {
						return new Symbol(sym.ID, yytext());
					}
					case -207:
						break;
					case 309: {
						return new Symbol(sym.ID, yytext());
					}
					case -208:
						break;
					case 310: {
						return new Symbol(sym.ID, yytext());
					}
					case -209:
						break;
					case 313: {
						return new Symbol(sym.ID, yytext());
					}
					case -210:
						break;
					case 315: {
						return new Symbol(sym.ID, yytext());
					}
					case -211:
						break;
					case 317: {
						return new Symbol(sym.ID, yytext());
					}
					case -212:
						break;
					case 318: {
						return new Symbol(sym.ID, yytext());
					}
					case -213:
						break;
					case 319: {
						return new Symbol(sym.ID, yytext());
					}
					case -214:
						break;
					case 320: {
						return new Symbol(sym.ID, yytext());
					}
					case -215:
						break;
					case 321: {
						return new Symbol(sym.ID, yytext());
					}
					case -216:
						break;
					case 322: {
						return new Symbol(sym.ID, yytext());
					}
					case -217:
						break;
					case 323: {
						return new Symbol(sym.ID, yytext());
					}
					case -218:
						break;
					case 327: {
						return new Symbol(sym.ID, yytext());
					}
					case -219:
						break;
					case 328: {
						return new Symbol(sym.ID, yytext());
					}
					case -220:
						break;
					case 329: {
						return new Symbol(sym.ID, yytext());
					}
					case -221:
						break;
					case 330: {
						return new Symbol(sym.ID, yytext());
					}
					case -222:
						break;
					case 331: {
						return new Symbol(sym.ID, yytext());
					}
					case -223:
						break;
					case 335: {
						return new Symbol(sym.ID, yytext());
					}
					case -224:
						break;
					case 336: {
						return new Symbol(sym.ID, yytext());
					}
					case -225:
						break;
					case 337: {
						return new Symbol(sym.ID, yytext());
					}
					case -226:
						break;
					case 338: {
						return new Symbol(sym.ID, yytext());
					}
					case -227:
						break;
					case 339: {
						return new Symbol(sym.ID, yytext());
					}
					case -228:
						break;
					case 341: {
						return new Symbol(sym.ID, yytext());
					}
					case -229:
						break;
					case 342: {
						return new Symbol(sym.ID, yytext());
					}
					case -230:
						break;
					case 343: {
						return new Symbol(sym.ID, yytext());
					}
					case -231:
						break;
					case 345: {
						return new Symbol(sym.ID, yytext());
					}
					case -232:
						break;
					case 346: {
						return new Symbol(sym.ID, yytext());
					}
					case -233:
						break;
					case 347: {
						return new Symbol(sym.ID, yytext());
					}
					case -234:
						break;
					case 348: {
						return new Symbol(sym.ID, yytext());
					}
					case -235:
						break;
					case 350: {
						return new Symbol(sym.ID, yytext());
					}
					case -236:
						break;
					case 351: {
						return new Symbol(sym.ID, yytext());
					}
					case -237:
						break;
					case 352: {
						return new Symbol(sym.ID, yytext());
					}
					case -238:
						break;
					case 353: {
						return new Symbol(sym.ID, yytext());
					}
					case -239:
						break;
					case 354: {
						return new Symbol(sym.ID, yytext());
					}
					case -240:
						break;
					case 356: {
						return new Symbol(sym.ID, yytext());
					}
					case -241:
						break;
					case 357: {
						return new Symbol(sym.ID, yytext());
					}
					case -242:
						break;
					case 358: {
						return new Symbol(sym.ID, yytext());
					}
					case -243:
						break;
					case 359: {
						return new Symbol(sym.ID, yytext());
					}
					case -244:
						break;
					case 360: {
						return new Symbol(sym.ID, yytext());
					}
					case -245:
						break;
					case 361: {
						return new Symbol(sym.ID, yytext());
					}
					case -246:
						break;
					case 362: {
						return new Symbol(sym.ID, yytext());
					}
					case -247:
						break;
					case 363: {
						return new Symbol(sym.ID, yytext());
					}
					case -248:
						break;
					case 364: {
						return new Symbol(sym.ID, yytext());
					}
					case -249:
						break;
					case 365: {
						return new Symbol(sym.ID, yytext());
					}
					case -250:
						break;
					case 366: {
						return new Symbol(sym.ID, yytext());
					}
					case -251:
						break;
					case 367: {
						return new Symbol(sym.ID, yytext());
					}
					case -252:
						break;
					case 368: {
						return new Symbol(sym.ID, yytext());
					}
					case -253:
						break;
					case 369: {
						return new Symbol(sym.ID, yytext());
					}
					case -254:
						break;
					case 370: {
						return new Symbol(sym.ID, yytext());
					}
					case -255:
						break;
					case 371: {
						return new Symbol(sym.ID, yytext());
					}
					case -256:
						break;
					case 372: {
						return new Symbol(sym.ID, yytext());
					}
					case -257:
						break;
					case 373: {
						return new Symbol(sym.ID, yytext());
					}
					case -258:
						break;
					case 374: {
						return new Symbol(sym.ID, yytext());
					}
					case -259:
						break;
					case 375: {
						return new Symbol(sym.ID, yytext());
					}
					case -260:
						break;
					default:
						yy_error(YY_E_INTERNAL, false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
