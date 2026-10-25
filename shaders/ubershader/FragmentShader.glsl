#version 460 core

out vec4 FragColor;
uniform int u_index;

vec3 MyColorFuncSin(vec2 uv, float k) {

    float r = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0);
    float g = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 2.094);
    float b = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 4.189);
    return vec3(k * r, k * g, k * b);
}
vec3 MyColorFuncCos(vec2 uv, float k) {

    float r = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0);
    float g = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 2.094);
    float b = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 4.189);
    return vec3(k * r, k * g, k * b);
}

void main() {
    vec2 uv = gl_FragCoord.xy / 800.0;
    float k = u_index / 100.0;

    vec3 res;

    //So many branches...
    if(u_index < 50) {
        if(u_index < 25) {
            if(u_index < 5) {
                if(u_index == 0) res = MyColorFuncCos(uv, k);
                if(u_index == 1) res = MyColorFuncSin(uv, k);
                if(u_index == 2) res = MyColorFuncSin(uv, k);
                if(u_index == 3) res = MyColorFuncSin(uv, k);
                if(u_index == 4) res = MyColorFuncSin(uv, k);
            }
            else if(u_index < 10) {
                if(u_index == 5) res = MyColorFuncCos(uv, k);
                if(u_index == 6) res = MyColorFuncSin(uv, k);
                if(u_index == 7) res = MyColorFuncSin(uv, k);
                if(u_index == 8) res = MyColorFuncSin(uv, k);
                if(u_index == 9) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 15) {
                if(u_index == 10) res = MyColorFuncSin(uv, k);
                if(u_index == 11) res = MyColorFuncCos(uv, k);
                if(u_index == 12) res = MyColorFuncCos(uv, k);
                if(u_index == 13) res = MyColorFuncCos(uv, k);
                if(u_index == 14) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 20) {
                if(u_index == 15) res = MyColorFuncSin(uv, k);
                if(u_index == 16) res = MyColorFuncSin(uv, k);
                if(u_index == 17) res = MyColorFuncCos(uv, k);
                if(u_index == 18) res = MyColorFuncSin(uv, k);
                if(u_index == 19) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 25) {
                if(u_index == 20) res = MyColorFuncSin(uv, k);
                if(u_index == 21) res = MyColorFuncSin(uv, k);
                if(u_index == 22) res = MyColorFuncCos(uv, k);
                if(u_index == 23) res = MyColorFuncSin(uv, k);
                if(u_index == 24) res = MyColorFuncCos(uv, k);
            }
        }
        else {
            if(u_index < 30) {
                if(u_index == 25) res = MyColorFuncSin(uv, k);
                if(u_index == 26) res = MyColorFuncSin(uv, k);
                if(u_index == 27) res = MyColorFuncCos(uv, k);
                if(u_index == 28) res = MyColorFuncSin(uv, k);
                if(u_index == 29) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 35) {
                if(u_index == 30) res = MyColorFuncSin(uv, k);
                if(u_index == 31) res = MyColorFuncCos(uv, k);
                if(u_index == 32) res = MyColorFuncSin(uv, k);
                if(u_index == 33) res = MyColorFuncSin(uv, k);
                if(u_index == 34) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 40) {
                if(u_index == 35) res = MyColorFuncCos(uv, k);
                if(u_index == 36) res = MyColorFuncSin(uv, k);
                if(u_index == 37) res = MyColorFuncCos(uv, k);
                if(u_index == 38) res = MyColorFuncSin(uv, k);
                if(u_index == 39) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 45) {
                if(u_index == 40) res = MyColorFuncSin(uv, k);
                if(u_index == 41) res = MyColorFuncSin(uv, k);
                if(u_index == 42) res = MyColorFuncCos(uv, k);
                if(u_index == 43) res = MyColorFuncSin(uv, k);
                if(u_index == 44) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 50) {
                if(u_index == 45) res = MyColorFuncSin(uv, k);
                if(u_index == 46) res = MyColorFuncCos(uv, k);
                if(u_index == 47) res = MyColorFuncCos(uv, k);
                if(u_index == 48) res = MyColorFuncSin(uv, k);
                if(u_index == 49) res = MyColorFuncCos(uv, k);
            }

        }
    }
    else {
        if(u_index < 75) {
            if(u_index < 55) {
                if(u_index == 50) res = MyColorFuncSin(uv, k);
                if(u_index == 51) res = MyColorFuncSin(uv, k);
                if(u_index == 52) res = MyColorFuncCos(uv, k);
                if(u_index == 53) res = MyColorFuncSin(uv, k);
                if(u_index == 54) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 60) {
                if(u_index == 55) res = MyColorFuncSin(uv, k);
                if(u_index == 56) res = MyColorFuncCos(uv, k);
                if(u_index == 57) res = MyColorFuncCos(uv, k);
                if(u_index == 58) res = MyColorFuncSin(uv, k);
                if(u_index == 59) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 65) {
                if(u_index == 60) res = MyColorFuncSin(uv, k);
                if(u_index == 61) res = MyColorFuncCos(uv, k);
                if(u_index == 62) res = MyColorFuncCos(uv, k);
                if(u_index == 63) res = MyColorFuncSin(uv, k);
                if(u_index == 64) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 70) {
                if(u_index == 65) res = MyColorFuncSin(uv, k);
                if(u_index == 66) res = MyColorFuncSin(uv, k);
                if(u_index == 67) res = MyColorFuncCos(uv, k);
                if(u_index == 68) res = MyColorFuncSin(uv, k);
                if(u_index == 69) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 75) {
                if(u_index == 70) res = MyColorFuncSin(uv, k);
                if(u_index == 71) res = MyColorFuncSin(uv, k);
                if(u_index == 72) res = MyColorFuncCos(uv, k);
                if(u_index == 73) res = MyColorFuncSin(uv, k);
                if(u_index == 74) res = MyColorFuncCos(uv, k);
            }
        }
        else {
            if(u_index < 80) {
                if(u_index == 75) res = MyColorFuncSin(uv, k);
                if(u_index == 76) res = MyColorFuncCos(uv, k);
                if(u_index == 77) res = MyColorFuncCos(uv, k);
                if(u_index == 78) res = MyColorFuncSin(uv, k);
                if(u_index == 79) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 85) {
                if(u_index == 80) res = MyColorFuncSin(uv, k);
                if(u_index == 81) res = MyColorFuncSin(uv, k);
                if(u_index == 82) res = MyColorFuncCos(uv, k);
                if(u_index == 83) res = MyColorFuncSin(uv, k);
                if(u_index == 84) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 90) {
                if(u_index == 85) res = MyColorFuncSin(uv, k);
                if(u_index == 86) res = MyColorFuncSin(uv, k);
                if(u_index == 87) res = MyColorFuncCos(uv, k);
                if(u_index == 88) res = MyColorFuncSin(uv, k);
                if(u_index == 89) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 95) {
                if(u_index == 90) res = MyColorFuncSin(uv, k);
                if(u_index == 91) res = MyColorFuncSin(uv, k);
                if(u_index == 92) res = MyColorFuncSin(uv, k);
                if(u_index == 93) res = MyColorFuncCos(uv, k);
                if(u_index == 94) res = MyColorFuncCos(uv, k);
            }
            else if(u_index < 100) {
                if(u_index == 95) res = MyColorFuncCos(uv, k);
                if(u_index == 96) res = MyColorFuncSin(uv, k);
                if(u_index == 97) res = MyColorFuncCos(uv, k);
                if(u_index == 98) res = MyColorFuncCos(uv, k);
                if(u_index == 99) res = MyColorFuncCos(uv, k);
            }
        }

    }


    FragColor = vec4(res, 1.0);



}
