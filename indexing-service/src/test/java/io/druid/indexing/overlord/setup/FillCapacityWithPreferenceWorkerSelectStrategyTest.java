/*
 * Druid - a distributed column store.
 * Copyright (C) 2012, 2013  Metamarkets Group Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.druid.indexing.overlord.setup;

import com.google.api.client.util.Sets;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import io.druid.indexing.common.task.NoopTask;
import io.druid.indexing.overlord.ImmutableZkWorker;
import io.druid.indexing.overlord.config.RemoteTaskRunnerConfig;
import io.druid.indexing.worker.Worker;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

public class FillCapacityWithPreferenceWorkerSelectStrategyTest
{
  @Test
  public void testFindWorkerForTask() throws Exception
  {
    FillCapacityWorkerSelectStrategy strategy = new FillCapacityWithPreferenceWorkerSelectStrategy(
        new FillCapacityWithPreferenceConfig()
        {
          @Override
          public Map<String, String> getPreferences()
          {
            return ImmutableMap.of("foo", "localhost");
          }
        },
        new RemoteTaskRunnerConfig()
    );

    Optional<ImmutableZkWorker> optional = strategy.findWorkerForTask(
        ImmutableMap.of(
            "localhost",
            new ImmutableZkWorker(
                new Worker("localhost", "localhost", 1, "v1"),
                0,
                Sets.<String>newHashSet()
            )
        ),
        new NoopTask(null, 1, 0, null, null)
    );
    ImmutableZkWorker worker = optional.get();
    Assert.assertEquals("localhost", worker.getWorker().getHost());
  }
}