/**
* Licensed to niosmtp developers ('niosmtp') under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* niosmtp licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package me.normanmaurer.niosmtp.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterator} implementation which wraps a given array
 * 
 * @author Norman Maurer
 *
 * @param <E>
 */
public class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;
    private int index = 0;
    
    public ArrayIterator(E[] array) {
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[index++];
            
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Throws {@link UnsupportedOperationException}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Read-only");
    }

}
