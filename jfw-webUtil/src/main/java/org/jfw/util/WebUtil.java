package org.jfw.util;

public abstract class WebUtil {
    public static String normalize(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        for (int i = 0; i < len; ++i) {
            if (chars[i] == '\\')
                chars[i] = '/';
        }
        if (chars[0] != '/') {
            char[] cc = new char[len + 1];
            cc[0] = '/';
            System.arraycopy(chars, 0, cc, 1, len);
            ++len;
        }
        for (int i = 0; i < len; ++i) {
            if ('/' != chars[i])
                continue;
            if (i + 1 < len) {
                char c = chars[i + 1];
                if (c == '/') {
                    System.arraycopy(chars, i + 2, chars, i + 1, len - i - 2);
                    --len;
                    --i;
                    continue;
                } else if (c == '.' && (i + 2 < len)) {
                    if (chars[i + 2] == '/') {
                        System.arraycopy(chars, i + 3, chars, i + 1, len - i - 3);
                        len -= 2;
                        ;
                        --i;
                        continue;
                    } else if ((i + 3 < len) && ('.' == chars[i + 2]) && ('/' == chars[i + 3])) {
                        int k = 0;
                        for (int j = i - 1; j >= 0; --i) {
                            if (chars[j] == '/') {
                                k = j;
                                break;
                            }
                        }
                        System.arraycopy(chars, i + 4, chars, k + 1, len - i - 4);
                        len = len - i - 3 + k; // len-i-4+k+1;
                        i = k - 1;
                    }
                }
            }
        }

        return new String(chars,0, len);
    }

}
