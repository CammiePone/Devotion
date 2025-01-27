#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Radius;
uniform float DevotionBlobsStepGranularity;

out vec4 fragColor;

void main(){
    vec4 alpha = texture(DiffuseSampler, texCoord);
    vec4 maxVal = alpha;
    float step = max(1, ceil(Radius / DevotionBlobsStepGranularity));

    for(float u = 0.0; u <= Radius; u += step) {
        for(float v = 0.0; v <= Radius; v += step) {
            if(maxVal.a <= 0) {
                float weight = (((sqrt(u * u + v * v) / (Radius)) > 1.0) ? 0.0 : 1.0);

                vec4 leftDown = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, -v * oneTexel.y));
                vec4 rightUp = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, v * oneTexel.y));
                vec4 rightDown = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, v * oneTexel.y));
                vec4 leftUp = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, -v * oneTexel.y));

                vec4 tmpMax0 = max(leftDown, rightUp);
                vec4 tmpMax1 = max(rightDown, leftUp);
                vec4 tempMax2 = max(tmpMax0, tmpMax1);
                maxVal = mix(maxVal, max(maxVal, tempMax2), weight);
            }
        }
    }

    fragColor = maxVal;
}
