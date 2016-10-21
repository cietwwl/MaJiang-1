package com.zxz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mina.common.IoSession;

import com.zxz.domain.Game;
import com.zxz.domain.User;

/**
 * ��Ϸ��ܼң�����һ��һ������Ϸ,key�Ƿ����id
 *
 */
public class GameManager {
	private static Logger logger = Logger.getLogger(StartServer.class);  
	public static Map<String,Game> map = new HashMap<String, Game>();
	
	public static void addGameMap(String gameId,Game game){
		map.put(gameId, game);
	}
	
	public static Map<String,Game> getGameMap(){
		return map;
	}
	
	public static List<IoSession> getSessionListWithRoomNumber(String roomNumber){
		Game game = map.get(roomNumber);
		List<IoSession> list = new ArrayList<IoSession>();
		if(game!=null){
			List<User> userList = game.getRoom().getUserList();
			for(User u:userList){
				list.add(u.getIoSession());
			}
			return list;
		}else{
			return null;
		}
	}
	
	/**���ݷ���ŵõ�����
	 * @param roomNumber
	 * @return
	 */
	public static Game getGameWithRoomNumber(String roomNumber){
		boolean containsKey = map.containsKey(roomNumber);
		if(containsKey){
			return map.get(roomNumber);
		}
		return null;
	}
	
	
	/**��ɢ����
	 * @param roomNumber
	 * @return
	 */
	public static Game removeGameWithRoomNumber(String roomNumber){
		boolean containsKey = map.containsKey(roomNumber);
		if(containsKey){
			logger.info("gameManager �����:"+roomNumber+"  ��ɢ");
			return map.remove(roomNumber);
		}else{
			logger.fatal("gameManager �Ƴ�����ʧ�� �����:"+roomNumber);
		}
		return null;
	}
}