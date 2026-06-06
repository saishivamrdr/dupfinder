package com.dupfinder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class ByteComparator {
    private static final int BUFFER_SIZE= 8192;

    private ByteComparator() {

    }
    public static boolean identical(Path first, Path second) throws IOException{
        if(Files.size(first) != Files.size(second)) {
            return false;
        }

        byte[] firstBuffer= new byte[BUFFER_SIZE];
        byte[] secondBuffer= new byte[BUFFER_SIZE];

        try(InputStream firstInput= Files.newInputStream(first);
            InputStream secondInput= Files.newInputStream(second)){
            int firstRead;

            while((firstRead= firstInput.read(firstBuffer))!= -1){
                int secondRead= secondInput.read(secondBuffer);
                if(firstRead!= secondRead){
                    return false;
                }
                if(!Arrays.equals(firstBuffer, 0, firstRead, secondBuffer, 0, secondRead)){
                    return false;
                }
            }
            return secondInput.read()== -1;
        }
    }
}