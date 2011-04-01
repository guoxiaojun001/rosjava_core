/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.internal.node.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;
import org.ros.internal.node.address.Address;
import org.ros.internal.node.address.AdvertiseAddress;
import org.ros.internal.node.address.BindAddress;
import org.ros.internal.node.xmlrpc.Node;

import java.io.IOException;
import java.net.URI;

/**
 * @author kwc@willowgarage.com (Ken Conley)
 */
public class NodeServerTest {

  class FakeNode implements Node {
  }

  @Test
  public void testGetPublicUri() throws XmlRpcException, IOException {
    BindAddress bindAddress = BindAddress.createPublic(0);
    NodeServer nodeServer = new NodeServer(bindAddress, new AdvertiseAddress("override"));
    try {
      nodeServer.getUri();
      fail("Should not have succeeded before startup.");
    } catch (RuntimeException e) {
    }

    nodeServer.start(FakeNode.class, new FakeNode());
    URI uri = nodeServer.getUri();
    assertEquals("override", uri.getHost());
    assertTrue(uri.getPort() > 0);

    nodeServer.shutdown();
  }
  
  @Test
  public void testGetPrivateUri() throws XmlRpcException, IOException {
    BindAddress bindAddress = BindAddress.createPrivate(0);
    NodeServer nodeServer = new NodeServer(bindAddress, AdvertiseAddress.createPrivate());
    try {
      nodeServer.getUri();
      fail("Should not have succeeded before startup.");
    } catch (RuntimeException e) {
    }

    nodeServer.start(FakeNode.class, new FakeNode());
    URI uri = nodeServer.getUri();
    assertEquals(Address.LOOPBACK, uri.getHost());
    assertTrue(uri.getPort() > 0);

    nodeServer.shutdown();
  }
  
}