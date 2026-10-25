#version 460 core

out vec4 FragColor;
uniform int u_index;

vec3 MyColorFunc(vec2 uv, float k) {

    float r = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0);
    float g = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 2.094);
    float b = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 4.189);
    return vec3(k * r, k * g, k * b);
}

void main() {
    vec2 uv = gl_FragCoord.xy / 800.0;

    FragColor = vec4(MyColorFunc(uv, 0.490000), 1.0);
}
