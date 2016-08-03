package com.hjf.ctit.entity;

import android.content.Context;

import com.hjf.ctit.sqlutils.Persistence;
import com.hjf.ctit.sqlutils.PersistenceException;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Connections是一个单例类,将所有的连接对象存储在一个集合中,它们可以在Activities间传递
 * 
 * @author HJF
 *
 */
public class Connections {

	// 单例模式
	private static Connections instance = null;

	private HashMap<String, Connection> connections = null;

	// 保存、删除、还原连接对象
	private Persistence persistence = null;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	private Connections(Context context) {
		connections = new HashMap<String, Connection>();

		// 尝试恢复连接状态
		persistence = new Persistence(context);
		try {
			List<Connection> l = persistence.restoreConnections(context);
			for (Connection c : l) {
				connections.put(c.handle(), c);
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 向外界提供一个入口，实例化一个Connections对象
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static Connections getInstance(Context context) {
		if (instance == null) {
			instance = new Connections(context);
		}

		return instance;
	}

	/**
	 * 查找返回一个Connection对象
	 * 
	 * @param handle
	 * @return
	 */
	public Connection getConnection(String handle) {

		return connections.get(handle);
	}

	/**
	 * 往集合中添加connection对象
	 * 
	 * @param connection
	 */
	public void addConnection(Connection connection) {
		connections.put(connection.handle(), connection);
		try {
			persistence.persistConnection(connection);
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a fully initialised <code>MqttAndroidClient</code> for the
	 * parameters given
	 * 
	 * @param context
	 *            The Applications context
	 * @param serverURI
	 *            The ServerURI to connect to
	 * @param clientId
	 *            The clientId for this client
	 * @return new instance of MqttAndroidClient
	 */
	public MqttAndroidClient createClient(Context context, String serverURI, String clientId) {
		MqttAndroidClient client = new MqttAndroidClient(context, serverURI, clientId);
		return client;
	}

	/**
	 * Get all the connections associated with this <code>Connections</code>
	 * object.
	 * 
	 * @return <code>Map</code> of connections
	 */
	public Map<String, Connection> getConnections() {
		return connections;
	}

	/**
	 * Removes a connection from the map of connections
	 * 
	 * @param connection
	 *            
	 */
	public void removeConnection(Connection connection) {
		connections.remove(connection.handle());
		persistence.deleteConnection(connection);
	}
//
//	public void removeAllConnection() {
//		persistence.deleteAllConnection();
//
//	}
}
