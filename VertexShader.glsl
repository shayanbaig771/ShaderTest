#version 330 core
layout (location = 0) in vec2 v_pos;


uniform mat4 v_model;
uniform mat4 v_view;
uniform mat4 v_projection;


void main() {
    gl_Position = v_projection * v_view * v_model * vec4(v_pos, 0.0, 1.0);
}