package com.phonepe.columbus.reflect;

interface SampleInterface {

    @SampleAnnotation(name = "sample")
    class SampleWithAnnotation implements SampleInterface {
    }

    class SampleWithoutAnnotation implements SampleInterface {
    }

    class SampleWithMixin implements SampleInterface {
    }

    @SampleAnnotation(name = "class")
    class SampleWithMixinOverride implements SampleInterface {
    }
}
