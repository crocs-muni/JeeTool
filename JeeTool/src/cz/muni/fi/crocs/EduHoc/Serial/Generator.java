/*
 * The MIT License
 *
 * Copyright 2016 lukemcnemee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import java.util.Random;

/**
 *
 * @author lukemcnemee
 */
public class Generator {
    private Random r;
    private Long seed;
    private Long counter;
    
    public Generator() {     
        Random gen = new Random();
        seed = gen.nextLong();
        r = new Random(seed);
        counter = (long) 0;
    }
    
    public Generator(String seed){
        this.seed = Long.valueOf(seed, 16);
        r = new Random(this.seed);
    }
    
    public Generator(Long seed) {
        this.seed = seed;
        r = new Random(seed);
    }

    public Long getSeed() {
        return seed;
    }
    
    public String getHexSeed(){
        return Long.toHexString(seed).toUpperCase();
    }
    
    public String getNextValue(){
        synchronized(this){
            counter++;
            return (counter + "#" + Integer.toHexString(r.nextInt()));            
        }
    }
    
}
