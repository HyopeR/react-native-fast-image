#import "FFFastImageBlurTransformation.h"
#import <CoreImage/CoreImage.h>

static const CGFloat BLUR_MIN_INPUT = 0.1;
static const CGFloat BLUR_MAX_INPUT = 10.0;
static const CGFloat BLUR_MAX_SCALE = 25.0;
static const CGFloat BLUR_MULTIPLIER = 1.5;

@implementation FFFastImageBlurTransformation

- (instancetype)initWithRadius:(CGFloat)radius {
    self = [super init];
    if (self) {
        _radius = [self normalizeBlurRadius:radius];
    }
    return self;
}

- (UIImage *)transform:(UIImage *)image {
    CIContext *context = [CIContext contextWithOptions:nil];

    CIImage *input = [CIImage imageWithCGImage:image.CGImage];

    CIFilter *clampFilter = [CIFilter filterWithName:@"CIAffineClamp"];
    [clampFilter setValue:input forKey:kCIInputImageKey];
    [clampFilter setValue:[NSValue valueWithCGAffineTransform:CGAffineTransformIdentity] forKey:@"inputTransform"];
    CIImage *clampedImage = clampFilter.outputImage;

    CIFilter *blurFilter = [CIFilter filterWithName:@"CIGaussianBlur"];
    [blurFilter setValue:clampedImage forKey:kCIInputImageKey];
    [blurFilter setValue:@(_radius) forKey:kCIInputRadiusKey];

    CIImage *output = [[blurFilter outputImage] imageByCroppingToRect:[input extent]];
    if (!output) return image;

    CGImageRef outputRef = [context createCGImage:output fromRect:[output extent]];
    if (!outputRef) return image;

    UIImage *outputBlurred = [UIImage imageWithCGImage:outputRef scale:image.scale orientation:image.imageOrientation];
    CGImageRelease(outputRef);
    return outputBlurred;
}

/**
* Clamp user-provided radius to 0.1–10.
* Then scale to a maximum of 25 for the blur script.
*/
- (CGFloat)normalizeBlurRadius:(CGFloat)radius {
    CGFloat clamped = fmax(BLUR_MIN_INPUT, fmin(radius, BLUR_MAX_INPUT));
    CGFloat base = (clamped / BLUR_MAX_INPUT) * BLUR_MAX_SCALE;
    return base * BLUR_MULTIPLIER;
}

@end
