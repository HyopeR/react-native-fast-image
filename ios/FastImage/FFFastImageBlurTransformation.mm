#import "FFFastImageBlurTransformation.h"
#import <CoreImage/CoreImage.h>

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

    CGFloat blurScale = MIN(image.size.width, image.size.height) / 1000.0;
    CGFloat blurRadius = _radius * blurScale;
    CIFilter *blurFilter = [CIFilter filterWithName:@"CIGaussianBlur"];
    [blurFilter setValue:clampedImage forKey:kCIInputImageKey];
    [blurFilter setValue:@(blurRadius) forKey:kCIInputRadiusKey];

    CIImage *output = [[blurFilter outputImage] imageByCroppingToRect:[input extent]];
    if (!output) return image;

    CGImageRef outputRef = [context createCGImage:output fromRect:[output extent]];
    if (!outputRef) return image;

    UIImage *outputBlurred = [UIImage imageWithCGImage:outputRef scale:image.scale orientation:image.imageOrientation];
    CGImageRelease(outputRef);
    return outputBlurred;
}

// Clamp user-provided radius to 0.1–10.
// Then scale to a maximum of 30 for the blur effect.
- (CGFloat)normalizeBlurRadius:(CGFloat)radius {
    CGFloat clamped = fmax(0.1, fmin(radius, 10.0));
    return fmin(30.0, (clamped / 10.0) * 30.0);
}

@end
