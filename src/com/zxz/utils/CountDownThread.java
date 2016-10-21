package com.zxz.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.zxz.controller.GameManager;
import com.zxz.controller.RoomManager;
import com.zxz.domain.Game;
import com.zxz.domain.OneRoom;
import com.zxz.domain.User;
import com.zxz.service.PlayGameService;
import com.zxz.service.UserService;

/**���Ƶ���ʱ�̣߳�������������Ƿ����,���û�г��ƣ�ϵͳ������ҳ���
 * @author Administrator
 */
public class CountDownThread implements Runnable,Constant{

	String roomId;//����� 
	
	
	/**
	 * ��ǰ�ľ���
	 */
	int currentGame;
	
	private static Logger logger = Logger.getLogger(CountDownThread.class);  
	
	public CountDownThread(String roomId,int currentGame) {
		super();
		this.roomId = roomId;
		this.currentGame = currentGame;
	}

	@Override
	public void run() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					//logger.info(".......�Զ�����......");
					Game game = getGame();
					if (game == null) {
						logger.fatal("���ӽ��̽���--game==null:" + "RoomId:" + roomId + " currentGame:" + currentGame);
						timer.cancel();//���̽��� 
						return;
					}
					Map<Integer, String> gameStatusMap = game.getGameStatusMap();
					String gameStatus = gameStatusMap.get(currentGame);
					if (gameStatus.equals(GAME_END)) {
						logger.fatal("���ӽ��̽���:" + "RoomId:" + roomId + " currentGame:" + currentGame);
						timer.cancel();//���̽��� 
						Thread.currentThread().stop();
						stopCurrentThread();
						return;
					}
					User user = getUser(game);
					if(user==null){
						logger.error("�����̣߳��û�Ϊ��..............");
					}
					Date lastChuPaiDate = user.getLastChuPaiDate();
					long interval = getInterval(lastChuPaiDate, new Date());//�õ�ʱ����
					int status = game.getGameStatus();//��Ϸ��״̬
					logStatus(status, interval, game);
					autoPlayWithGameStatus(game, status, interval);
				} catch (Exception e) {
					e.printStackTrace();
					logger.fatal("�����̷߳�������"+e);
					timer.cancel();//���̽��� 
					Thread.currentThread().stop();
					stopCurrentThread();
				}
			}
		},0,5000);
		logger.info("���ӽ��̽���:"+"RoomId:"+roomId+" currentGame:"+currentGame +"��Ľ���.");
	}
	
	/**
	 * @param status
	 */
	public void logStatus(int status,long interval,Game game){
		if(status==GAGME_STATUS_OF_CHUPAI){
			User user = game.getSeatMap().get(game.getDirec());
			int hashCode = user.hashCode();
			System.out.println("�Զ�����user hashCode:"+hashCode+" "+ user.getUserName());
			List<Integer> myPlays = user.getMyPlays();
			System.out.println("-------------------"+ user.getUserName()+"---------------------------");
			Algorithm2.showPai(myPlays);
			System.out.println("-----------------------������-----------------------");
			logger.info("��ǰ��״̬��ֵ��:"+status+" ״̬�ǡ����ơ�"+"ʱ������:"+interval+"����"+"	���Ƶķ�����:"+game.getDirec()+" ���Ƶ��˳�ȥ������:"+myPlays);
		}else if(status==GAGME_STATUS_OF_PENGPAI){
			logger.info("��ǰ��״̬��ֵ��:"+status+" ״̬�ǡ����ơ�"+"ʱ������:"+interval+"����"+"	�������Ƶ��û�:"+ game.getCanPengUser());
		}else if(status==GAGME_STATUS_OF_GANGPAI){
			logger.info("��ǰ��״̬��ֵ��:"+status+" ״̬�ǡ��Ӹܡ�"+"ʱ������:"+interval+"����"+"	���ԽӸܵ��û�"+game.getCanGangUser());
		}else if(status==GAGME_STATUS_OF_ANGANG){
			logger.info("��ǰ��״̬��ֵ��:"+status+" ״̬�ǡ����ܡ�"+"ʱ������:"+interval+"����"+"	���԰��ܵ��û�"+game.getCanAnGangUser());
		}else if(status==GAGME_STATUS_OF_GONG_GANG){
			logger.info("��ǰ��״̬��ֵ��:"+status+" ״̬�ǡ����ܡ�"+"ʱ������:"+interval+"����"+"	���Թ��ܵ��û�"+game.getCanGongGangUser());
		}else{
			logger.info("δ֪״̬:"+status);
		}
	}
	
	/**����
	 * @param game
	 */
	private void anGang(Game game) {
		User canAnGangUser = game.getCanAnGangUser();
		List<Integer> anGangCards = game.getAnGangCards();
		canAnGangUser.userGangCards(anGangCards);
		logger.info("�Զ�����...����.................:"+Algorithm2.showPai(anGangCards));
		canAnGangUser.recordUserGangCards(1, anGangCards);
		PlayGameService.notifyAllUserAnGang(game, anGangCards,canAnGangUser);//֪ͨ���е���Ҹܵ��� 
		PlayGameService.modifyUserScoreForAnGang(game, canAnGangUser);//�޸���Ұ��ܵ÷�
		//�������ץһ���� 
		PlayGameService.userDrawCard(game, canAnGangUser.getDirection());
	}
	
	/**����,���û�����û�г��ƺ󣬸��û�����й�״̬
	 * @param game
	 */
	public void chuPai(Game game){
		String direc = game.getDirec();
		Map<String, User> seatMap = game.getSeatMap();
		User user = seatMap.get(direc);
		user.setUserCanPlay(true);
		int cardId = user.autoChuPai();//�Զ�������
		//�����û�û�г��ƵĴ���
		int totalNotPlay = user.getTotalNotPlay();
		if(totalNotPlay<2){
			totalNotPlay = totalNotPlay +1;
			user.setTotalNotPlay(totalNotPlay);
			if(totalNotPlay==2){//����û�г��������й�
				logger.info("���û�����û�г��ƣ��Զ��й�...:"+user.getUserName());
				user.setAuto(true);//���û������й�
				JSONObject autoJsonObject = UserService.getAutoJsonObject(user);
				OneRoom oneRoom = RoomManager.getRoomMap().get(user.getRoomId());
				oneRoom.noticeUsersWithJsonObject(autoJsonObject);
			}
		}
		logger.info("�Զ�����....................:"+CardsMap.getCardType(cardId));
		JSONObject outJsonObject = PlayGameService.getChuPaiOutJSONObject(cardId, user);
		NotifyTool.notifyIoSessionList(GameManager.getSessionListWithRoomNumber(user.getRoomId()), outJsonObject);//֪ͨ�����û�������� ��ʲô
		PlayGameService.analysis(cardId, user, game);
	}
	
	
	/**����
	 * @param game
	 */
	public void pengPai(Game game){
		User canPengUser = game.getCanPengUser();
		Integer autoPengCardId = game.getAutoPengCardId();//�Զ����Ƶļ��� 
		List<Integer> cards = canPengUser.getCards();
		List<Integer> pengList = PlayGameService.getPengList(cards, autoPengCardId);//�õ��������Ƶļ���
		if(pengList.size()<2){//�����������ƣ��Ŷ�
			//FIXME  Ϊʲô����뵽����
			logger.fatal("���������е���......:"+canPengUser);
			logger.fatal("Ϊʲô������������ֻ��һ���� ���Ƶļ�����:"+pengList);
			game.setGameStatus(GAGME_STATUS_OF_CHUPAI);
			return;
		}
		canPengUser.userPengCards(pengList);
		pengList.add(autoPengCardId);
		logger.info("�Զ�����...................."+Algorithm2.showPai(pengList));
		canPengUser.addUserPengCards(pengList);//�û�������������
		PlayGameService.notifyAllUserPeng(game, pengList,canPengUser);
		game.setDirec(canPengUser.getDirection());//���¸ı���Ϸ�ķ���
		//���ƺ���Ϸ��״̬��Ϊ����
		game.setGameStatus(GAGME_STATUS_OF_CHUPAI);
		canPengUser.setLastChuPaiDate(new Date());
		canPengUser.setUserCanPlay(true);//�û����Դ���
	}
	
	/**����
	 * @param game
	 */
	public void gangPai(Game game){
		User canGangUser = game.getCanGangUser();
		Integer autoGangCardId = game.getAutoGangCardId();
		List<Integer> cards = canGangUser.getCards();
		List<Integer> gangCards = PlayGameService.getGangList(cards, autoGangCardId);
		canGangUser.userGangCards(gangCards);
		gangCards.add(autoGangCardId);
		logger.info("�Զ�����....................:"+Algorithm2.showPai(gangCards));
		//��¼��Ҹܵ���
		canGangUser.recordUserGangCards(0, gangCards);
		PlayGameService.modifyUserScoreForGang(game, canGangUser);//�޸���ҵ÷�
		PlayGameService.notifyAllUserGang(game, gangCards,canGangUser);//֪ͨ���е���Ҹܵ��� 
		//�������ץһ���� 
		PlayGameService.userDrawCard(game, canGangUser.getDirection());
	}
	
	
	private Game getGame() {
		Map<String, Game> gameMap = GameManager.getGameMap();
		Game game = gameMap.get(roomId);
		return game;
	}
	
	
	public long getInterval(Date before,Date nowDate){
		long lastTime = before.getTime();
		long nowTime = nowDate.getTime();
		return nowTime-lastTime;
	}
	
	/**�õ��û�
	 * @param game
	 * @return
	 */
	private User getUser(Game game) {
		String direc = game.getDirec();
		Map<String, User> seatMap = game.getSeatMap();
		User user = seatMap.get(direc);
		return user;
	}
	
	private void autoPlayWithGameStatus(Game game, int status, long interval) {
		switch (status) {
		case GAGME_STATUS_OF_CHUPAI:
			if(interval>30000){
				chuPai(game);//����
			}
			break;
		case GAGME_STATUS_OF_PENGPAI:
			if(interval>10000){
				pengPai(game);//����
			}
			break;
		case GAGME_STATUS_OF_GANGPAI:
			if(interval>10000){
				gangPai(game);//�Ӹ�
			}
			break;	
		case GAGME_STATUS_OF_ANGANG:	
			if(interval>10000){
				anGang(game);//����
			}
			break;
		case GAGME_STATUS_OF_GONG_GANG:	
			if(interval>10000){
				gongGang(game);//����
			}
			break;
		}
	}

	/**�Զ�����
	 * @param game
	 */
	private void gongGang(Game game) {
		User user = game.getCanGongGangUser();
		Integer cardId = game.getGongGangCardId();
		List<Integer> pengCards = user.getPengCards();//�û�������
		List<Integer> removeList = PlayGameService.getRemoveList(cardId, pengCards);
		for(int i=0;i<removeList.size();i++){
			Integer revomeCard = removeList.get(i);
			pengCards.remove(revomeCard);
		}
		removeList.add(cardId);
		logger.info("�Զ�����....................:"+Algorithm2.showPai(removeList));
		//���Լ��������Ƴ����ܵ�������
		user.removeCardFromGongGang(cardId);
		//��¼��Ҹܵ���
		user.recordUserGangCards(2, removeList);
		PlayGameService.notifyAllUserGongGang(game, removeList,user);//֪ͨ���е���Ҹܵ��� 
		PlayGameService.modifyUserScoreForGongGang(game, user);//�޸���ҹ��ܵ÷�
		//�������ץһ���� 
		PlayGameService.userDrawCard(game, user.getDirection());
	}
	
	private void stopCurrentThread(){
		Thread.currentThread().stop();
	}
	
}