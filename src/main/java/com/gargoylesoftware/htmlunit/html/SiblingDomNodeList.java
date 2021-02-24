/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * An implementation of DomNodeList that is much less expensive for iteration.
 *
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
class SiblingDomNodeList extends AbstractSequentialList<DomNode> implements DomNodeList<DomNode> {

    private DomNode parent_;

    SiblingDomNodeList(final DomNode parent) {
        parent_ = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        int length = 0;
        for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
            length++;
        }
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index) {
        return get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode get(final int index) {
        int i = 0;
        for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (i == index) {
                return node;
            }
            i++;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<DomNode> listIterator(final int index) {
        return new SiblingListIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SiblingDomNodeList[" + parent_ + "]";
    }

    private class SiblingListIterator implements ListIterator<DomNode> {
        private DomNode prev_;
        private DomNode next_;
        private int nextIndex_;

        SiblingListIterator(final int index) {
            next_ = parent_.getFirstChild();
            nextIndex_ = 0;
            for (int i = 0; i < index; i++) {
                next();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return next_ != null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DomNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev_ = next_;
            next_ = next_.getNextSibling();
            nextIndex_++;
            return prev_;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasPrevious() {
            return prev_ != null;
        }

        @Override
        public DomNode previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            next_ = prev_;
            prev_ = prev_.getPreviousSibling();
            nextIndex_--;
            return next_;
        }

        @Override
        public int nextIndex() {
            return nextIndex_;
        }

        @Override
        public int previousIndex() {
            return nextIndex_ - 1;
        }

        @Override
        public void add(final DomNode e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final DomNode e) {
            throw new UnsupportedOperationException();
        }
    }
}
