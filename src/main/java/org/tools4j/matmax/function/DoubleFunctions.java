/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 matmax (tools4j.org) Marco Terzer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tools4j.matmax.function;

import org.tools4j.matmax.indexed.Double2D;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public final class DoubleFunctions {

    public static final DoubleUnaryOperator NEGATE = d -> -d;
    public static final DoubleUnaryOperator INVERT = d -> 1/d;
    public static final DoubleUnaryOperator SIGNUM = Math::signum;
    public static final DoubleUnaryOperator ABS = Math::abs;
    public static final DoubleUnaryOperator SQRT = Math::sqrt;

    public static Function<Double2D, Double2D> TRANSPOSE = mx -> (r, c) -> mx.valueAsDouble(c, r);


    public static DoubleUnaryOperator add(final double summand) {
        return d -> d + summand;
    }

    public static DoubleUnaryOperator subtract(final double subtrahend) {
        return d -> d - subtrahend;
    }

    public static DoubleUnaryOperator multiply(final double factor) {
        return d -> d * factor;
    }

    public static DoubleUnaryOperator divide(final double divisor) {
        return d -> d / divisor;
    }

    public static DoubleUnaryOperator pow(final double exponent) {
        return d -> Math.pow(d, exponent);
    }

    public static Function<Double2D, Double2D> lag(final int rows, final int cols) {
        return mx -> (r, c) -> mx.valueAsDouble(r - rows, c - cols);
    }


    private DoubleFunctions() {
        throw new RuntimeException("No DoubleFunctions for you!");
    }
}
