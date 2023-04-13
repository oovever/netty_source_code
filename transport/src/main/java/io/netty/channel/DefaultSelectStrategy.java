/*
 * Copyright 2016 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.IntSupplier;

/**
 * Default select strategy.
 */
final class DefaultSelectStrategy implements SelectStrategy {
    static final SelectStrategy INSTANCE = new DefaultSelectStrategy();

    private DefaultSelectStrategy() { }

    /**
     *
     *  private final IntSupplier selectNowSupplier = new IntSupplier() {
     *        执行非阻塞的select方法;队列中有任务，执行非阻塞select方法，快速处理队列中的任务
     *
     *         @Override
     *         public int get() throws Exception {
     *         返回就绪的 channel 的数量，可以为 0
     *              return selectNow();
     *          }
     *     };
     */
    @Override
    public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
//     存在任务，走选择器selectSupplier.get()  不存在任务返回 -1
        return hasTasks ? selectSupplier.get() : SelectStrategy.SELECT;
    }
}
