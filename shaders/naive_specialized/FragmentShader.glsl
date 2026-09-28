#version 330 core

out vec4 FragColor;

void main()
{
    vec2 uv = gl_FragCoord.xy / 800.0;

    float r = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0);
    float g = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 2.094);
    float b = 0.5 + 0.5 * sin(uv.x * 10.0 + uv.y * 6.0 + 4.189);

    FragColor = vec4(r, g, b, 1.0);
}
