#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;
in vec4 vPosition;

uniform float Radius;
uniform float DevotionTransStepGranularity;
uniform vec3 CameraPosition;
uniform vec3 Center;
uniform mat4 ProjectionMatrix;
uniform mat4 ModelViewMatrix;
uniform ivec4 ViewPort;

out vec4 fragColor;

vec4 screenToWorld(mat4 matr, in float depth, in vec2 uv){
    vec4 coord = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    coord = inverse(matr) * coord;
    coord.xyz /= coord.w; // linearize
    return coord;
}
vec3 worldToScreen(mat4 matr, in vec4 screenCoord) {
    vec4 coord = screenCoord;
    coord.xyz *= coord.w; // de-linearize
    coord = matr * coord;
    coord = coord * 0.5 + 0.5; // map back to [0,1] range
    return coord.xyz;
}

// TODO I cannot, for the life of me, remember what i did last time to accidentally change the thickness of the bands
void main(){
    vec4 center = texture(DiffuseSampler, texCoord);
    float distanceToTransparency = 1.0;

    if(center.a == 0) {
        fragColor = vec4(0);
        return;
    }

    float sceneDepth = texture(DepthSampler, texCoord).x;
    vec4 pixelPosition = screenToWorld(ProjectionMatrix, sceneDepth, texCoord.xy);
    vec4 offsetPosition = pixelPosition + vec4(1, 1, 0, 0);
    vec3 offsetCoord = worldToScreen(ProjectionMatrix, offsetPosition);

    vec2 texelOffset = offsetCoord.xy - texCoord.xy;
    texelOffset /= 170.0;

    float step = max(1.0, ceil(Radius / DevotionTransStepGranularity));

    for(float u = 0.0; u <= Radius; u += step) {
        for(float v = 0.0; v <= Radius; v += step) {
            float distanceFromCenter = sqrt(u * u + v * v) / Radius;

            if(distanceFromCenter < distanceToTransparency) {
                float s0 = texture(DiffuseSampler, texCoord + vec2(-u * texelOffset.x, -v * texelOffset.y)).a;
                float s1 = texture(DiffuseSampler, texCoord + vec2(u * texelOffset.x, v * texelOffset.y)).a;
                float s2 = texture(DiffuseSampler, texCoord + vec2(-u * texelOffset.x, v * texelOffset.y)).a;
                float s3 = texture(DiffuseSampler, texCoord + vec2(u * texelOffset.x, -v * texelOffset.y)).a;

                if(s0 <= 0 || s1 <= 0 || s2 <= 0 || s3 <= 0) {
                    distanceToTransparency = distanceFromCenter;
                }
            }
        }
    }

    distanceToTransparency = min(abs(distanceToTransparency), 0.9);
    fragColor = vec4(vec3(center), center.a * (1 - distanceToTransparency));
}
