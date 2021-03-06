package com.ldbc.datachecker.failure;

import java.io.File;
import java.util.Arrays;

import com.ldbc.datachecker.ColumnCheckException;
import com.ldbc.datachecker.DirectoryCheck;
import com.ldbc.datachecker.DirectoryCheckException;
import com.ldbc.datachecker.FailedCheckPolicy;
import com.ldbc.datachecker.FileCheck;
import com.ldbc.datachecker.FileCheckException;

public class TerminateFailedCheckPolicy implements FailedCheckPolicy
{
    @Override
    public FailedColumnCheckPolicy getFailedColumnCheckPolicy( FileCheck fileCheck, long lineNumber, String[] row )
    {
        return new TerminateFailedColumnCheckPolicy( fileCheck, lineNumber, row );
    }

    @Override
    public FailedFileCheckPolicy getFailedFileCheckPolicy()
    {
        return new TerminateFailedFileCheckPolicy();
    }

    @Override
    public FailedDirectoryCheckPolicy getFailedDirectoryCheckPolicy()
    {
        return new TerminateFailedDirectoryCheckPolicy();
    }

    public static class TerminateFailedColumnCheckPolicy extends FailedColumnCheckPolicy
    {
        public TerminateFailedColumnCheckPolicy( FileCheck fileCheck, long lineNumber, String[] row )
        {
            super( fileCheck, lineNumber, row );
        }

        public void handleFailedColumnCheck( String columnString, String message ) throws ColumnCheckException
        {
            throw new ColumnCheckException( String.format(
                    "\nCheck[%s] File[%s] Line[%s] Row[%s] Column[%s] Message[%s]",
                    getFileCheck().getClass().getSimpleName(), getFileCheck().forFile().getAbsolutePath(),
                    getLineNumber(), Arrays.toString( getRow() ), columnString, message ) );
        }
    }

    public static class TerminateFailedFileCheckPolicy extends FailedFileCheckPolicy
    {
        @Override
        public void handleFailedLineCheck( FileCheck fileCheck, String message, long lineNumber, String[] line )
                throws FileCheckException
        {
            throw new FileCheckException( String.format( "\nCheck[%s] File[%s] Line[%s] Content[%s] Message[%s]",
                    fileCheck.getClass().getSimpleName(), fileCheck.forFile(), lineNumber, Arrays.toString( line ),
                    message ) );
        }

        @Override
        public void handleFailedFileCheck( FileCheck fileCheck, String message ) throws FileCheckException
        {
            throw new FileCheckException( String.format( "\nCheck[%s] File[%s] Message[%s]",
                    fileCheck.getClass().getSimpleName(), fileCheck.forFile(), message ) );
        }
    }

    public static class TerminateFailedDirectoryCheckPolicy extends FailedDirectoryCheckPolicy
    {
        public void handleFailedDirectoryCheck( DirectoryCheck directoryCheck, File directory, String message )
                throws DirectoryCheckException
        {
            throw new DirectoryCheckException( String.format( "\nCheck[%s] Directory[%s] Message[%s]",
                    directoryCheck.getClass().getSimpleName(), directory.getAbsolutePath(), message ) );
        }
    }
}
