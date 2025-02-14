#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform mat4 DevotionProjectionMatrix;
uniform float Radius;
uniform float DevotionBlobsStepGranularity;

in vec2 texCoord;

out vec4 fragColor;

vec4 screenToWorld(mat4 matr, in float depth, in vec2 uv) {
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

void main() {
    vec4 alpha = texture(DiffuseSampler, texCoord);
    vec4 maxVal = alpha;
    vec4 pixelPosition = screenToWorld(DevotionProjectionMatrix, 0.99, texCoord.xy);
    vec4 offsetPosition = pixelPosition + vec4(1, 1, 0, 0); // TODO I want to be modifying the xy of this vec4 depending on distance of the reference pixel i think?
    vec3 offsetCoord = worldToScreen(DevotionProjectionMatrix, offsetPosition);
    vec2 texelOffset = offsetCoord.xy - texCoord.xy;
    float step = max(1, ceil(Radius / DevotionBlobsStepGranularity));

    texelOffset /= 170.0;

    for(float u = 0.0; u <= Radius; u += step) {
        for(float v = 0.0; v <= Radius; v += step) {
            if(maxVal.a <= 0) {
                float weight = (((sqrt(u * u + v * v) / (Radius)) > 1.0) ? 0.0 : 1.0);

                vec4 leftDown = texture(DiffuseSampler, texCoord + vec2(-u * texelOffset.x, -v * texelOffset.y));
                vec4 rightUp = texture(DiffuseSampler, texCoord + vec2(u * texelOffset.x, v * texelOffset.y));
                vec4 rightDown = texture(DiffuseSampler, texCoord + vec2(-u * texelOffset.x, v * texelOffset.y));
                vec4 leftUp = texture(DiffuseSampler, texCoord + vec2(u * texelOffset.x, -v * texelOffset.y));

                vec4 tmpMax0 = max(leftDown, rightUp);
                vec4 tmpMax1 = max(rightDown, leftUp);
                vec4 tempMax2 = max(tmpMax0, tmpMax1);
                maxVal = mix(maxVal, max(maxVal, tempMax2), weight);
            }
        }
    }

    fragColor = maxVal;
}
