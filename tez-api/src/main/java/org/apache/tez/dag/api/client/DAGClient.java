/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.tez.dag.api.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.tez.dag.api.TezException;
import org.apache.tez.dag.api.Vertex;

/*
 * Interface class for monitoring the <code>DAG</code> running in a Tez DAG
 * Application Master.
 */
public interface DAGClient extends Closeable {

  /**
   * Get the YARN ApplicationId for the app running the DAG
   * @return <code>ApplicationId</code>
   */
  public ApplicationId getApplicationId();

  @Private
  /**
   * Get the YARN ApplicationReport for the app running the DAG. For performance
   * reasons this may be stale copy and should be used to access static info. It
   * may be null.
   * @return <code>ApplicationReport</code> or null
   */
  public ApplicationReport getApplicationReport();

  /**
   * Get the status of the specified DAG
   * @param statusOptions Optionally, retrieve additional information based on
   *                      specified options
   */
  public DAGStatus getDAGStatus(Set<StatusGetOpts> statusOptions)
      throws IOException, TezException;

  /**
   * Get the status of a Vertex of a DAG
   * @param statusOptions Optionally, retrieve additional information based on
   *                      specified options
   */
  public VertexStatus getVertexStatus(String vertexName,
      Set<StatusGetOpts> statusOptions)
    throws IOException, TezException;

  /**
   * Kill a running DAG
   *
   */
  public void tryKillDAG() throws IOException, TezException;

  /**
   * Wait for DAG to complete without printing any vertex statuses
   * 
   * @return Final DAG Status
   * @throws IOException
   * @throws TezException
   */
  public DAGStatus waitForCompletion() throws IOException, TezException;

  /**
   * Wait for DAG to complete and print the selected vertex status periodically.
   * 
   * @param vertexNames
   *          which vertex details to print; null mean no vertex status and it
   *          is equivalent to call <code>waitForCompletion()</code>
   * @param statusGetOpts
   *          : status get options. For example, to get counter pass
   *          <code>EnumSet.of(StatusGetOpts.GET_COUNTERS)</code>
   * @return Final DAG Status
   * @throws IOException
   * @throws TezException
   */
  public DAGStatus waitForCompletionWithStatusUpdates(@Nullable Set<Vertex> vertices,
      @Nullable Set<StatusGetOpts> statusGetOpts) throws IOException, TezException;

  /**
   * Wait for DAG to complete and periodically print *all* vertices' status.
   * 
   * @param statusGetOpts
   *          : status get options. For example, to get counter pass
   *          <code>EnumSet.of(StatusGetOpts.GET_COUNTERS)</code>
   * @return Final DAG Status
   * @throws IOException
   * @throws TezException
   */
  DAGStatus waitForCompletionWithAllStatusUpdates(@Nullable Set<StatusGetOpts> statusGetOpts)
      throws IOException, TezException;
}
