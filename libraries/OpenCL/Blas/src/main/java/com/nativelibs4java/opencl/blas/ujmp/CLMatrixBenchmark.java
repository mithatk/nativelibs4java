/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nativelibs4java.opencl.blas.ujmp;

import com.nativelibs4java.opencl.blas.CLDefaultMatrix2D;
import com.nativelibs4java.opencl.blas.CLKernels;
import com.nativelibs4java.opencl.blas.CLMatrixUtils;
import org.bridj.Pointer;
import static org.bridj.Pointer.*;

import org.ujmp.core.Matrix;
import org.ujmp.core.benchmark.AbstractMatrix2DBenchmark;
import org.ujmp.core.doublematrix.DoubleMatrix2D;
import org.ujmp.core.exceptions.MatrixException;

/**
 *
 * @author ochafik
 */
public class CLMatrixBenchmark extends AbstractMatrix2DBenchmark {

    @Override
    public DoubleMatrix2D createMatrix(long... size) throws MatrixException {
        return new CLDenseDoubleMatrix2D(size);
    }

    @Override
    public DoubleMatrix2D createMatrix(Matrix source) throws MatrixException {
        if (source instanceof CLDenseDoubleMatrix2D)
            return (DoubleMatrix2D)((CLDenseDoubleMatrix2D)source).copy();
        else {
            DoubleMatrix2D dsource = (DoubleMatrix2D)source;
            long rows = dsource.getRowCount(), columns = dsource.getColumnCount();
            CLDenseDoubleMatrix2D copy = new CLDenseDoubleMatrix2D(rows, columns);
            long stride = copy.getStride();
            Pointer<Double> b = allocateDoubles(rows * stride).order(CLKernels.getInstance().getContext().getKernelsDefaultByteOrder());
            for (long i = 0; i < rows; i++) {
            		long offset = i * stride;
                for (long j = 0; j < columns; j++) {
                    b.set(offset + i, dsource.getDouble(i, j));
                }
            }
            copy.write(b);
            return copy;
        }
    }

}
