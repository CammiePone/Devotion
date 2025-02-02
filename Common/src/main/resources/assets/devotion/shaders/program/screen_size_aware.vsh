#version 150

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;
uniform vec2 BaseSize;

in vec4 Position;

out vec2 texCoord;
out vec2 oneTexel;
out vec4 vPosition;

void main(){
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    vec2 sizeFactor = InSize / BaseSize;

    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    oneTexel = sizeFactor / InSize;
    vPosition = gl_Position;
    texCoord = Position.xy / OutSize;
}
