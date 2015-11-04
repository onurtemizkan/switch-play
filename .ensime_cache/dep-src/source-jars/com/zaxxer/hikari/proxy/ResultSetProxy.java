/*
 * Copyright (C) 2013, 2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zaxxer.hikari.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * This is the proxy class for java.sql.ResultSet.
 *
 * @author Brett Wooldridge
 */
public abstract class ResultSetProxy implements ResultSet
{
   protected final ConnectionProxy connection;
   protected final ResultSet delegate;

   protected ResultSetProxy(ConnectionProxy connection, ResultSet resultSet)
   {
      this.connection = connection;
      this.delegate = resultSet;
   }

   protected final SQLException checkException(SQLException e)
   {
      return connection.checkException(e);
   }

   // **********************************************************************
   //                 Overridden java.sql.ResultSet Methods
   // **********************************************************************

   /** {@inheritDoc} */
   @Override
   public void updateRow() throws SQLException
   {
      connection.markCommitStateDirty();
      delegate.updateRow();
   }

   /** {@inheritDoc} */
   @Override
   public void insertRow() throws SQLException
   {
      connection.markCommitStateDirty();
      delegate.insertRow();
   }

   /** {@inheritDoc} */
   @Override
   public void deleteRow() throws SQLException
   {
      connection.markCommitStateDirty();
      delegate.deleteRow();
   }

   /** {@inheritDoc} */
   @Override
   @SuppressWarnings("unchecked")
   public final <T> T unwrap(Class<T> iface) throws SQLException
   {
      if (iface.isInstance(delegate)) {
         return (T) delegate;
      }
      else if (delegate instanceof Wrapper) {
          return (T) delegate.unwrap(iface);
      }

      throw new SQLException("Wrapped ResultSet is not an instance of " + iface);
   }   
}
