package com.zxz.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.zxz.algorithm.InitialPuKe;


public class AlgorithmOld {
	private static Logger logger = Logger.getLogger(Algorithm.class);
	public static void main(String[] args) throws Exception {
		int tingpaiArray [] = {12,13,36,40,45,60,69,72,76,80,81,108,109};
//		int paiArray[] = {5, 6, 11, 15, 16, 62, 71, 72, 73, 75, 108};
		long currentTimeMillis1 = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());
		int paiArray[] = {24, 26, 39, 40, 47, 73, 75, 78, 82, 86, 108};
		showPai(paiArray);
		boolean win = isWin2(paiArray);
		if(win){
			System.out.println("hu");
		}
		long currentTimeMillis2 = System.currentTimeMillis();
		System.out.println(currentTimeMillis2-currentTimeMillis1);
	}
	
	public static boolean isWin2(int array[]) {
		if(array.length==2){
			boolean winWithDouble = isWinWithDouble(array);
			if(winWithDouble){
				return true;
			}else{
				return false;
			}
		}
		return otherCheck(array);
	}

	/**
	 * @param array
	 * @return
	 */
	private static boolean otherCheck(int[] array) {
		
		return checkWithDuiZiList(array);
	}


	/**
	 * @param array
	 * @param duiziList
	 * @return
	 */
	private static boolean checkWithDuiZiList(int[] array) {
			
		boolean isWin = check(array);
				
		return isWin;
	}
	
	public static boolean check(int[] array) {
		int[] arr = getArrayWithoutDuiZi(array);
		List<Integer> hongZhongList = getCanUseHongZhongList(arr);//�õ����õĺ���
		List<Integer> cards = getListWithoutHongZhong(arr);//ȥ�������еĺ���
		cards = getCardsWithoutSen(cards);// 345 5 567 233445-2345
		cards = getCardsWithoutCan(cards);
		boolean removeOneDuiZi = removeOneDuiZi(cards,hongZhongList);
		if(!removeOneDuiZi){
			return check3(array);
		}
		showPai(cards);
		if(cards.size()==0){
			return true;
		}else{
			int index = 0;
			int length = cards.size();
			boolean checkOver = false;
			while(!checkOver){
				if(index>=length){
					checkOver = true;
					break;
				}
				int n1 =  cards.get(index);
				int n2 = -1;
				if(index+1<length){
					n2 = cards.get(index+1);
				}
				//�ú���������
				if(hongZhongList.size()<=0){//û�к��� 
					return check3(array);//2�� 2�� 3�� 4�� 5�� 7Ͳ 9Ͳ 1�� 1�� 1�� ���� 
				}else{
					//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
					String type1 = CardsMap.getCardType(n1);
					String type2 = CardsMap.getCardType(n2);
					int interval = getInterval(n1, n2);
					if(type1.equals(type2)){//����һ��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ��������");
					}else if(interval==1||interval==2){//���������һ�� ����1��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ����������");
					}else{//������������
						
						if(index+1>=length&&hongZhongList.size()>=1){
							return true;
						}
						
						if(hongZhongList.size()<2){
							System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
							return false;
						}else{
							hongZhongList.remove(0);
							hongZhongList.remove(0);
							index = index +1;
							System.out.println("��������:"+CardsMap.getCardType(n1));
						}
					}
				}
					
			}
		}
		return true;
	}
	
	/**		
	 *2�� 2�� 3�� 4�� 5�� 5�� 6�� 7�� 
	 * @param array
	 * @return
	 */
	public static boolean check4(int[] array){
		int[] arr = getArrayWithoutDuiZi(array);
		List<Integer> hongZhongList = getCanUseHongZhongList(arr);//�õ����õĺ���
		List<Integer> cards = getListWithoutHongZhong(arr);//ȥ�������еĺ���
		boolean removeOneDuiZi = removeOneDuiZi(cards,hongZhongList);
		if(!removeOneDuiZi){
			return false;
		}
		cards = getCardsWithoutSen(cards);// 345 5 567 233445-2345
		cards = getCardsWithoutCan(cards);
		showPai(cards);
		if(cards.size()==0){
			return true;
		}else{
			int index = 0;
			int length = cards.size();
			boolean checkOver = false;
			while(!checkOver){
				if(index>=length){
					checkOver = true;
					break;
				}
				int n1 =  cards.get(index);
				int n2 = -1;
				if(index+1<length){
					n2 = cards.get(index+1);
				}
			
				//�ú���������
				if(hongZhongList.size()<=0){//û�к��� 
					return false;
				}else{
					//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
					String type1 = CardsMap.getCardType(n1);
					String type2 = CardsMap.getCardType(n2);
					int interval = getInterval(n1, n2);
					if(type1.equals(type2)){//����һ��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ��������");
					}else if(interval==1||interval==2){//���������һ�� ����1��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ����������");
					}else{//������������
						
						if(index+1>=length&&hongZhongList.size()>=1){
							return true;
						}
						
						if(hongZhongList.size()<2){
							System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
							return false;
						}else{
							hongZhongList.remove(0);
							hongZhongList.remove(0);
							index = index +1;
							System.out.println("��������:"+CardsMap.getCardType(n1));
						}
					}
				}
					
			}
		}
		return true;
	}
	
	
	/**		
	 *2�� 2�� 3�� 4�� 5�� 5�� 6�� 7�� 
	 * @param array
	 * @return
	 */
	public static boolean check3(int[] array){
		int[] arr = getArrayWithoutDuiZi(array);
		List<Integer> hongZhongList = getCanUseHongZhongList(arr);//�õ����õĺ���
		List<Integer> cards = getListWithoutHongZhong(arr);//ȥ�������еĺ���
		boolean removeOneDuiZi = removeOneDuiZi(cards,hongZhongList);
		if(!removeOneDuiZi){
			return false;
		}
		cards = getCardsWithoutSen(cards);// 345 5 567 233445-2345
		cards = getCardsWithoutCan(cards);
		showPai(cards);
		if(cards.size()==0){
			return true;
		}else{
			int index = 0;
			int length = cards.size();
			boolean checkOver = false;
			while(!checkOver){
				if(index>=length){
					checkOver = true;
					break;
				}
				int n1 =  cards.get(index);
				int n2 = -1;
				if(index+1<length){
					n2 = cards.get(index+1);
				}
			
				//�ú���������
				if(hongZhongList.size()<=0){//û�к��� 
					return false;
				}else{
					//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
					String type1 = CardsMap.getCardType(n1);
					String type2 = CardsMap.getCardType(n2);
					int interval = getInterval(n1, n2);
					if(type1.equals(type2)){//����һ��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ��������");
					}else if(interval==1||interval==2){//���������һ�� ����1��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ����������");
					}else{//������������
						
						if(index+1>=length&&hongZhongList.size()>=1){
							return true;
						}
						
						if(hongZhongList.size()<2){
							System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
							return false;
						}else{
							hongZhongList.remove(0);
							hongZhongList.remove(0);
							index = index +1;
							System.out.println("��������:"+CardsMap.getCardType(n1));
						}
					}
				}
					
			}
		}
		return true;
	}
	
	
	//4�� 4�� [1Ͳ 2Ͳ 3Ͳ] [7Ͳ  ����  9Ͳ]  [1�� 2�� 3��] 1�� ����  3�� 
		public static boolean check2(int[] array) {
			int[] arr = getArrayWithoutDuiZi(array);
			List<Integer> hongZhongList = getCanUseHongZhongList(array);//�õ����õĺ���
			List<Integer> cards = getListWithoutHongZhong(array);//ȥ�������еĺ���
			cards = getCardsWithoutSen(cards);// 345 5 567 233445-2345
			//showPai(cards);
			cards = getCardsWithoutCan(cards);
			//showPai(cards);
			removeOneDuiZi(cards,hongZhongList);
			showPai(cards);
			
			if(cards.size()==0){
				return true;
			}else{
				int index = 0;
				int length = cards.size();
				boolean checkOver = false;
				while(!checkOver){
					if(index>=length){
						checkOver = true;
						break;
					}
					int n1 =  cards.get(index);
					int n2 = -1;
					if(index+1<length){
						n2 = cards.get(index+1);
					}
				
					//�ú���������
					if(hongZhongList.size()<=0){//û�к��� 
						return false;
					}else{
						//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
						String type1 = CardsMap.getCardType(n1);
						String type2 = CardsMap.getCardType(n2);
						int interval = getInterval(n1, n2);
						if(type1.equals(type2)){//����һ��
							index = index +2;
							hongZhongList.remove(0);
							System.out.println("����һ��������");
						}else if(interval==1||interval==2){//���������һ�� ����1��
							index = index +2;
							hongZhongList.remove(0);
							System.out.println("����һ����������");
						}else{//������������
							
							if(index+1>=length&&hongZhongList.size()>=1){
								return true;
							}
							
							if(hongZhongList.size()<2){
								System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
								return false;
							}else{
								hongZhongList.remove(0);
								hongZhongList.remove(0);
								index = index +1;
								System.out.println("��������:"+CardsMap.getCardType(n1));
							}
						}
					}
						
				}
			}
			
			boolean result = true;
			
			return result;
		}
	
	/**ɾ��һ������
	 * @param cards
	 * @param hongZhongList 
	 */
	private static boolean removeOneDuiZi(List<Integer> cards, List<Integer> hongZhongList) {
		List<Integer> list = new ArrayList<>();
		for(int i=0;i<cards.size();i++){
			String beforType = "";
			Integer cardId = cards.get(i);
			Integer beforeCardId = null;
			if(i>=1){
				beforeCardId = cards.get(i-1);
				beforType = CardsMap.getCardType(beforeCardId);
			}
			String currentType = CardsMap.getCardType(cardId);
			if(currentType.equals(beforType)){
				list.add(cardId);
				list.add(beforeCardId);
				break;
			}
		}
		
		if(list.size()>0){
			for(Integer card:list){
				cards.remove(card);
			}
			return true;
		}else{//˵��û�ж���,��Ҫ�Ӻ��������һ���� 
			cards.remove(0);
			if(hongZhongList.size()<1){
				return false;
			}
			hongZhongList.remove(0);
			return true;
		}
	}


	public static List<Integer> getCardsWithoutCan(List<Integer> list) {
		 List<Integer> canList = getCanList(list);
		 if(canList.size()>0){
			 list = removeListFromList(list, canList);
		 }
		 return list;
	}


	/**
	 * @param list
	 * @return
	 */
	private static List<Integer> getCanList(List<Integer> list) {
		List<Integer> result = new LinkedList<>();
		for(int i=0;i<list.size();){
			if(i>=list.size()){
				break;
			}
			int n1 = list.get(i);
			int n2 = -1;
			int n3 = -2;
			if(i+1<list.size()){
				n2 = list.get(i+1);
			}
			if(i+2<list.size()){
				n3 = list.get(i+2);
			}
			boolean oneSentence = isOneCan(n1, n2, n3);
			if(oneSentence){
				i = i+3;
				result.add(n1);
				result.add(n2);
				result.add(n3);
			}else{
				i = i+1;
			}
		}
		return result;
	}


	/**ȥ�����еľ���
	 * @param cards
	 * @return 
	 * @return 
	 */
	public static List<Integer> getCardsWithoutSen(List<Integer> cards) {//233445  112233  2344456
		//showPai(cards);
		List<Integer> notDistinctList = getNotDistinctWithoutSen(cards);//2345
		//System.out.println("���ظ�����");
		//showPai(notDistinctList);
		List<Integer> sentensList = getSentensList(notDistinctList);//234
		//showPai(sentensList);
		while(sentensList.size()>0){
			//System.out.println("��Ҫ�޳�����");
			//showPai(sentensList);
			cards = removeListFromList(cards,sentensList);//345
			//System.out.println("�޳���");
			//showPai(cards);
			notDistinctList = getNotDistinctWithoutSen(cards);//2345
			//System.out.println("���ظ�����");
			//showPai(notDistinctList);
			sentensList = getSentensList(notDistinctList);
		}
		return cards;
	}

	/**�������Ƴ�����
	 * @param cards
	 * @param sentensList
	 */
	private static List<Integer> removeListFromList(List<Integer> cards,
			List<Integer> sentensList) {
		List<Integer> list = new LinkedList<>();
		for(int i=0;i<cards.size();i++){
			Integer card = cards.get(i);
			boolean canAdd = true;
			for(int j=0;j<sentensList.size();j++){
				Integer remove = sentensList.get(j);
				if(card == remove){
					canAdd = false;
					break;
				}
			}
			if(canAdd){
				list.add(card);
			}
		}
		return list;
	}


	private static List<Integer> getSentensList(List<Integer> list) {
		List<Integer> result = new LinkedList<>();
		for(int i=0;i<list.size();){
			if(i>=list.size()){
				break;
			}
			int n1 = list.get(i);
			int n2 = -1;
			int n3 = -2;
			if(i+1<list.size()){
				n2 = list.get(i+1);
			}
			if(i+2<list.size()){
				n3 = list.get(i+2);
			}
			boolean oneSentence = isOneSentence(n1, n2, n3);
			if(oneSentence){
				i = i+3;
				result.add(n1);
				result.add(n2);
				result.add(n3);
			}else{
				i = i+1;
			}
		}
		return result;
	}
	
	
	
	/**ȥ�����еľ���
	 * @param cards
	 */
	private static List<Integer> getNotDistinctWithoutSen(List<Integer> cards) {
		String type = "";
		List<Integer> list = new LinkedList<>();
		for(int i=0;i<cards.size();i++){
			Integer card = cards.get(i);
			String cardType = CardsMap.getCardType(card);
			if(!type.equals(cardType)){
				list.add(card);
				type = cardType;
			}
		}
		return list;
	}

	//4�� 4�� [1Ͳ 2Ͳ 3Ͳ] [7Ͳ  ����  9Ͳ]  [1�� 2�� 3��] 1�� ����  3�� 
	public static boolean check2(int[] array,int num1,int num2) {
		int[] arr = getArrayWithoutDuiZi(array);//�õ��������������
		List<Integer> hongZhongList = getCanUseHongZhongList(arr);//�õ����õĺ���
		List<Integer> cards = getListWithoutHongZhong(arr);//ȥ�������еĺ���
		boolean result = true;
		List<Integer> removeList = getAllCanOrSenList(cards);//�õ����еĿ������Ǿ��ӵļ���
		cards = removeList(cards, removeList);//�Ƴ������
		showPai(cards);
		//7Ͳ 9Ͳ 1�� 2�� 2�� 3�� 3�� 
		cards = getSecondRemoveList(cards);
		showPai(cards);
		boolean checkOver = false;
		int index = 0;
		int length = cards.size();
		while(!checkOver){
			if(index>=length){
				checkOver = true;
				break;
			}
			int n1 =  cards.get(index);
			int n2 = -1;
			if(index+1<length){
				n2 = cards.get(index+1);
			}
			int n3 = -2;
			if(index+2<length){
				n3 = cards.get(index+2);
			}
			boolean oneCan = isOneCan(n1, n2, n3);
			boolean oneSentence = isOneSentence(n1, n2, n3);
			if(oneCan||oneSentence){//��õ����
				index = index +3;
				if(oneCan){
					System.out.println("����:"+CardsMap.getCardType(n1));
				}else if(oneSentence){
					System.out.println("����:"+CardsMap.getCardType(n1)+CardsMap.getCardType(n2)+CardsMap.getCardType(n3));
				}
			}else{
				//�ú���������
				if(hongZhongList.size()<=0){//û�к��� 
					return false;
				}else{
					//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
					String type1 = CardsMap.getCardType(n1);
					String type2 = CardsMap.getCardType(n2);
					int interval = getInterval(n1, n2);
					if(type1.equals(type2)){//����һ��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ��������");
					}else if(interval==1||interval==2){//���������һ�� ����1��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ����������");
					}else{//������������
						if(hongZhongList.size()<2){
							System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
							return false;
						}else{
							hongZhongList.remove(0);
							hongZhongList.remove(0);
							index = index +1;
							System.out.println("��������:"+CardsMap.getCardType(n1));
						}
					}
				}
			}
		}
		return result;
	}
	
	
	public static List<Integer> getSecondRemoveList(List<Integer> cards){
		List<Integer> result = new LinkedList<Integer>();
		System.out.println("cards");
		showPai(cards);
		System.out.println("cards");
		String type = "";
		for(int i=0;i<cards.size();i++){
			Integer card = cards.get(i);
			String cardType = CardsMap.getCardType(card);
			if(!cardType.equals(type)){
				result.add(card);
				type = cardType;
			}
		}
		showPai(result);
		showPai(result);
		List<Integer> firstRemoveList = getAllCanOrSenList(result);
		if(firstRemoveList.size()>0){
			List<Integer> removeList = removeList(cards, firstRemoveList);
			return removeList;
		}else{
			return cards;
		}
	}
	
	public static List<Integer> removeList(List<Integer> cards,List<Integer> removeList){
		List<Integer> result = new LinkedList<Integer>();
		//int lastRemove = -1;
		for(int i=0;i<cards.size();i++){
			Integer oneCard= cards.get(i);
            boolean canAdd = true;
			for(int j=0;j<removeList.size();j++){
				if(removeList.get(j)==oneCard){
					canAdd = false;
					break;
//					if(lastRemove!=oneCard){
//						lastRemove = oneCard;
//					}
				}
			}
			if(canAdd){
				result.add(oneCard);
			}
		}
		return result;
	}
	
	
	
	public static List<Integer> getAllCanOrSenList(List<Integer> list){
		int len = list.size();
		List<Integer> listResult = new LinkedList<Integer>();
		for(int i=0;i<list.size();){
			int n1 =  list.get(i);
			int n2 = -1;
			int n3 = -2;
			if(i+1<len){
				n2 = list.get(i+1);
			}
			if(i+2<len){
				n3 = list.get(i+2);
			}
			boolean oneCan = isOneCan(n1, n2, n3);
			boolean oneSentence = isOneSentence(n1, n2, n3);
			if(oneCan||oneSentence){
				listResult.add(n1);
				listResult.add(n2);
				listResult.add(n3);
				System.out.println(CardsMap.getCardType(n1)+"----"+CardsMap.getCardType(n2)+"------"+CardsMap.getCardType(n3));
				i = i+3;
			}else{
				i++;
			}
		}
		return listResult;
	}
	
	
	
	
	/**�õ�����list
	 * @param array
	 * @return
	 */
	public static List<Integer> getCanUseHongZhongList(int []array){
		List<Integer> hongZhongList =  new LinkedList<Integer>();
		for(int i=0;i<array.length;i++){
			if(array[i]>=108&&array[i]<=111){
				hongZhongList.add(array[i]);
			}
		}
		return hongZhongList;
	}
	
	
	/**
	 * �ж��û��Ƿ����
	 * @throws Exception 
	 */
	public static boolean isWin(List<Integer> list){
		int array[] = new int[list.size()];
		for(int i=0;i<list.size();i++){
			array[i] = list.get(i);
		}
		return isWin2(array);
	}
	
	


	/**�ж��Ƿ����
	 * @param array �������жϸ��û��Ƿ����
	 * @return
	 * @throws Exception 
	 */
	public static boolean isWin(int array[]) {
		showPai(array);
		if(array.length==2){
			boolean winWithDouble = isWinWithDouble(array);
			if(winWithDouble){
				logger.info("�ö��Ӻ���");
				return true;
			}else{
				logger.info("�ö��Ӷ�û����");
				return false;
			}
		}
		List<Integer> duiziList = getDuiZiList(array);
		
		if(duiziList.size()==0){
			logger.info("�����Ӷ�û��");
			return false;
		}
		
		int size = duiziList.size();
		if(duiziList.size()>0){
			for(int i=0;i<size/2;i++){
				int dui1 = duiziList.get(i*2);
				int dui2 = duiziList.get(i*2+1);
				boolean isWin = checkWinWithDuizi3(array, dui1,dui2);
				System.out.println("������:"+InitialPuKe.map.get(dui1)+" "+InitialPuKe.map.get(dui2));
				if(isWin){
					System.out.println("++����:++");
					return isWin;
				}
				System.out.println("---------------------------------------------");
			}
		}
		return false;
	}
	
	/**�����Ӻ���
	 * @param array
	 * @return
	 */
	public static boolean isWinWithDouble(int array[]){
		if(array.length==2){
			if(CardsMap.getCardType(array[0]).equals(CardsMap.getCardType(array[1]))){
				return true;
			}else if(array[0]>=108&&array[0]<=111){
				return true;
			}else if(array[1]>=108&&array[1]<=111){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**�Ƿ���һ������
	 * @param array
	 * @return ���ӵ����� ��:1��
	 */
	public static List<Integer> getDuiZiList(int array[]){
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < array.length-1; i++) {
			String previousType = "";
			if(i>0){
				previousType = InitialPuKe.map.get(array[i-1]);//�õ�ǰһ�ŵ����� 
			}
			String type = InitialPuKe.map.get(array[i]);//�õ���������
			String nextPukeType = InitialPuKe.map.get(array[i+1]);//��һ���Ƶ�����
			String nextTwoType = "";
			if(i==array.length-2){
				nextTwoType = "";//�¶����Ƶ�����
			}else{
				nextTwoType = InitialPuKe.map.get(array[i+2]);//�¶����Ƶ�����
			}
			if(type.equals(nextPukeType)){//&&!type.equals(nextTwoType)&&!type.equals(previousType)){//�����ƺ���һ����Ȳ����¶�����Ȳ��Һ�ǰһ�Ų����
				result.add(array[i]);
				result.add(array[i+1]);
				//System.out.println("������:"+type);
//				resultArray[0] = array[i];
//				resultArray[1] = array[i+1];
				//break;
			//1�� 1�� 1�� 2�� 3�� 7�� 7�� 7�� 8Ͳ 8Ͳ 8Ͳ 8�� 8�� 8�� 
			}else if(type.equals(nextPukeType)&&type.equals(nextTwoType)){//�����ƺ͵�һ������Ⱥ͵�������Ҳ���
				boolean oneSentence = false;
				if(i+4<array.length-1){
					 oneSentence = isOneSentence(array[i+2],array[i+3],array[i+4]);
				}
				if(oneSentence){//�����������ƺͺ�������Ƿ���һ������
					result.add(array[i]);
					result.add(array[i+1]);
				}
			}else if(nextPukeType.equals("����")||type.equals("����")){
				result.add(array[i]);
				result.add(array[i+1]);
			}
		}
		return result;
	}
	
	
	public static String showPai(int array[]){
		System.out.println("\t\t");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<array.length;i++){
			System.out.print(InitialPuKe.map.get(array[i])+" ");
			sb.append(InitialPuKe.map.get(array[i])+" ");
		}
		System.out.println();
		return sb.toString();
	}
	
	
	public static String showPai(List<Integer> cards){
		int []array = new int[cards.size()];
		for(int i=0;i<cards.size();i++){
			array[i] = cards.get(i);
		}
		String result = showPai(array);
		return result;
	}
	
	
	public static boolean checkWinWithDuizi3(int[] array,int num1,int num2) {
		int[] arr = getArrayWithoutDuiZi(array);//�õ��������������
		List<Integer> hongZhongList = getHongZhongList(array,num1,num2);//�õ����õĺ���
		List<Integer> listWithoutHongZhong = getListWithoutHongZhong(arr);
		boolean checkOver = false;
		boolean result = true;
		int index = 0;
		int length = listWithoutHongZhong.size();
		while(!checkOver){
			if(index>=listWithoutHongZhong.size()){
				checkOver = true;
				break;
			}
			//9, 29, 35, 36, 45, 48, 67,[ 69, 70], 89, 98, 102, 104,        <108>
			//3�� 8��   9��     1Ͳ    3Ͳ    4Ͳ    8Ͳ      9Ͳ 9Ͳ        5��     7��     8��     9��                         ���� 
			//����������
			//9, 29, 35, 36, 45, 48, 67, 89, 98, 102, 104
			//3�� 8��   9��     1Ͳ    3Ͳ    4Ͳ    8Ͳ   5��    7��    8��       9��   
			int n1 =  listWithoutHongZhong.get(index);
			int n2 = -1;
			int n3 = -2;
			if(index+1<length){
				n2 = listWithoutHongZhong.get(index+1);
			}
			if(index+2<length){
				n3 = listWithoutHongZhong.get(index+2);
			}
			boolean oneCan = isOneCan(n1, n2, n3);
			boolean oneSentence = isOneSentence(n1, n2, n3);
			if(oneCan||oneSentence){//��õ����
				index = index +3;
				
				if(oneCan){
					System.out.println("����:"+CardsMap.getCardType(n1));
				}else if(oneSentence){
					System.out.println("����:"+CardsMap.getCardType(n1)+CardsMap.getCardType(n2)+CardsMap.getCardType(n3));
				}
				
			}else{//�ú���������
				if(hongZhongList.size()<=0){//û�к��� 
					return false;
				}else{
					//�������ļ�������  1�� 1�� ������ 1�� 2�� ������ 1�� 3�� ����1 ��
					String type1 = CardsMap.getCardType(n1);
					String type2 = CardsMap.getCardType(n2);
					int interval = getInterval(n1, n2);
					if(type1.equals(type2)){//����һ��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ��������");
					}else if(interval==1||interval==2){//���������һ�� ����1��
						index = index +2;
						hongZhongList.remove(0);
						System.out.println("����һ����������");
					}else{//������������
						if(hongZhongList.size()<2){
							System.out.println("��ֹ�ĵط�:"+CardsMap.getCardType(n1));
							return false;
						}else{
							hongZhongList.remove(0);
							hongZhongList.remove(0);
							index = index +1;
							System.out.println("��������:"+CardsMap.getCardType(n1));
						}
					}
				}
			}
			
		}
		
		//�鿴ʣ��ĺ�����
		if(hongZhongList.size()%3!=0){
			System.out.println("�������ʣ:"+hongZhongList.size()+"û�к���");
			return false;
		}
		
		return result;
	}
	
	
	
	
	
	public static boolean checkWinWithDuizi2(int[] array,int num1,int num2) {
		int[] arr = getArrayWithoutDuiZi(array);
		List<Integer> hongZhongList = getHongZhongList(array,num1,num2);
		List<Integer> listWithoutHongZhong = getListWithoutHongZhong(arr);
		boolean checkOver = false;
		int index = 0;
		while(!checkOver){
			if(index>=listWithoutHongZhong.size()){
				checkOver = true;
				break;
			}
			Integer card1= listWithoutHongZhong.get(index);
			int card2 = -1;
			int card3 = -2;
			if(index+1<listWithoutHongZhong.size()){
				card2 = listWithoutHongZhong.get(index+1);
			}
			if(index+2<listWithoutHongZhong.size()){
				card3 = listWithoutHongZhong.get(index+2);
			}
			boolean oneSentence = isOneSentence(card1, card2, card3);
			boolean oneCan = isOneCan(card1, card2, card3);
			if(oneSentence||oneCan){
				index += 3;
			}else{
				int size = hongZhongList.size();
				if(size>0){
					boolean oneSentenceWithHongZhongList = isOneSentenceWithHongZhongList(card1, card2, hongZhongList);
					boolean oneCanWithHongZhongList = isOneCanWithHongZhongList(card1, card2, hongZhongList);
					if(oneSentenceWithHongZhongList||oneCanWithHongZhongList){
						hongZhongList.remove(0);
						index += 2;
					}else if(hongZhongList.size()>=2){
						hongZhongList.remove(0);
						hongZhongList.remove(0);
						index ++;
					}else{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	
	
	
	
	/**�õ�û�к��еĵļ���
	 * @param arr
	 */
	public static List<Integer> getListWithoutHongZhong(int[] arr) {
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]<108){
				list.add(arr[i]);
			}
		}
		return list;
	}

	/**�õ�����list
	 * @param array
	 * @return
	 */
	public static List<Integer> getHongZhongList(int []array,int num1,int num2){
		List<Integer> hongZhongList =  new LinkedList<Integer>();
		for(int i=0;i<array.length;i++){
			if(array[i]>=108&&array[i]<=111){
				if(array[i]!=num1&&array[i]!=num2){
					hongZhongList.add(array[i]);
				}
			}
		}
		return hongZhongList;
	}
	
	/**�����ǲ���һ����
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public static boolean isOneCanWithHongZhongList(int num1,int num2,List<Integer> hongzhongList){
		String type1 = CardsMap.getCardType(num1);
		String type2 = CardsMap.getCardType(num2);
		if(type1.equals(type2)){
			if(hongzhongList.size()>0){
				//hongzhongList.remove(0);
				return true;
			}
		}
		return false;
	}
	
	
	/**�����ǲ���һ����
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public static boolean isOneCan(int num1,int num2,int num3){
		String type1 = CardsMap.getCardType(num1);
		String type2 = CardsMap.getCardType(num2);
		String type3 = CardsMap.getCardType(num3);
		
		if(type1.equals(type2)&&type1.equals(type3)){
			//System.out.println("��:"+CardsMap.getCardType(num1));
			return true;
		}
		
		return false;
	}
	
	
	public static int[] getArrayWithoutDuiZi(int[] array){
		
		if(array.length<=2){
			throw new RuntimeException("���鳤�Ȳ���С��2");
		}
		
		int arr[] = new int[array.length];
		
		for(int i=0;i<array.length;i++){
				arr[i] = array[i];
		}
		
		return arr;
	}
	
	
	
	
	/**
	 * @param array
	 * @param isWin
	 * @param duiziList
	 * @return
	 */
	private static boolean checkWinWithDuizi(int[] array,
			int dui1,int dui2) {
		boolean isWin = true;
		boolean isCheckOver = false;
		int begin = 0;
		while(!isCheckOver){
			if(array[begin]!=dui1&&array[begin]!=dui2){
				String currentPaiHao = InitialPuKe.map.get(array[begin]);
				String nextPaiHao = InitialPuKe.map.get(array[begin+1]);
				String nextTwoHao = InitialPuKe.map.get(array[begin+2]);
				if(currentPaiHao.equals(nextPaiHao)&&currentPaiHao.equals(nextTwoHao)){//�����Ƿ���һ����
					System.out.println("��:"+currentPaiHao);
					if(begin+2<array.length-1){
						begin=begin+2;
					}
				}else{//�Ƿ��Ǿ���
					int a1 = array[begin];
					int a2 = array[begin+1];
					int a3 = array[begin+2];
					boolean isOneSentence = isOneSentence(a1,a2,a3);
					if(isOneSentence){
						System.out.println("����:"+InitialPuKe.map.get(a1)+" "+InitialPuKe.map.get(a2)+" "+InitialPuKe.map.get(a3));
						begin=begin+2;
					}else{
						System.out.println("���Ǿ���:"+InitialPuKe.map.get(a1)+" "+InitialPuKe.map.get(a2)+" "+InitialPuKe.map.get(a3));
						isWin = false;//�Ȳ��ǿ�Ҳ���Ǿ��ӣ�������
						isCheckOver =  true;
					}
				}
				begin+=1;
			}else{
				begin+=2;
			}
			if(begin==array.length-2){
				isCheckOver = true;
			}
		}
		return isWin;
	}
	
	/**�����ǲ���һ������
	 * @param a1
	 * @param a2
	 * @param a3
	 * @return
	 */
	public static boolean isOneSentence(int a1,int a2,int a3){
		String typeA1 = getTypeString(a1);
		String typeA2 = getTypeString(a2);
		String typeA3 = getTypeString(a3);
		if(!typeA1.equals(typeA2)||!typeA1.equals(typeA3)||!typeA2.equals(typeA3)){
			return false;
		}
		int typeA1Int = getTypeInt(a1);
		int typeA2Int = getTypeInt(a2);
		int typeA3Int = getTypeInt(a3);
		if(typeA2Int-typeA1Int!=1){
			return false;
		}
		if(typeA3Int-typeA2Int!=1){
			return false;
		}
		return true;
	}
	
	/**
	 * @param a1
	 * @param a2
	 * @return ������Ͳ�����򷵻�-1,���򷵻�����֮��ļ��
	 */
	public static int getInterval(int a1,int a2){
		String typeA1 = getTypeString(a1);
		String typeA2 = getTypeString(a2);
		
		if(!typeA1.equals(typeA2)){
			return -1;
		}
		
		int typeA1Int = getTypeInt(a1);
		int typeA2Int = getTypeInt(a2);
		
		return typeA2Int-typeA1Int;
		
	}
	
	/**�����ǲ���һ������
	 * @param a1
	 * @param a2
	 * @param a3
	 * @return
	 */
	public static boolean isOneSentenceWithHongZhongList(int a1,int a2,List<Integer> hongzhongList){
		String typeA1 = getTypeString(a1);
		String typeA2 = getTypeString(a2);
		if(!typeA1.equals(typeA2)){
			return false;
		}
		int typeA1Int = getTypeInt(a1);
		int typeA2Int = getTypeInt(a2);
		if(typeA2Int-typeA1Int==1){
			if(hongzhongList.size()>0){
				//hongzhongList.remove(0);
				return true;
			}
		}else if(typeA2Int-typeA1Int==2){
			if(hongzhongList.size()>0){
				//hongzhongList.remove(0);
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	/**�����ƺż�����Ƶ�����
	 * @param paiHao
	 * @return
	 */
	public static int getTypeInt(int paiHao) {
		int type = 0;
		if(paiHao>=0&&paiHao<=35){//��
			type=(paiHao/4)+1;
		}else if(paiHao>=36&&paiHao<=71){
			type=((paiHao/4)-9)+1;
		}else if(paiHao>=72&&paiHao<=107){
			type=((paiHao/4)-18)+1;
		}
		return type;
	}
	
	/**�����ƺż�����Ƶ�����
	 * @param paiHao
	 * @return
	 */
	public static String getTypeString(int paiHao) {
		String type = "";
		if(paiHao>=0&&paiHao<=35){//��
			type="��";
		}else if(paiHao>=36&&paiHao<=71){
			type="Ͳ";
		}else if(paiHao>=72&&paiHao<=107){
			type="��";
		}
		return type;
	}
}