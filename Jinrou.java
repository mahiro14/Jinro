import java.util.Arrays;

public class Jinrou {
	
    public static void main (String[] args) throws Exception{
    	//初期設定
        System.out.println("人数を入力してください");
        int numbers = new java.util.Scanner(System.in).nextInt();
        
        System.out.println("プレイヤーの名前を入力して下さい");
        final String[] player = new String[numbers];
        for( int i=0 ; i<numbers ; i++ ){
            System.out.print(i+1+" ");
            player[i] = new java.util.Scanner(System.in).nextLine();
        }
        
        System.out.print("人狼の人数:");
        int wolf_capa = new java.util.Scanner(System.in).nextInt();
        System.out.print("市民の人数:");
        int civil_capa = new java.util.Scanner(System.in).nextInt();
        while( wolf_capa+civil_capa!=numbers){
            System.out.println("人数が一致していません。もう一度入力してください。\n");
            System.out.print("人狼の人数:");
            wolf_capa = new java.util.Scanner(System.in).nextInt();
            System.out.print("市民の人数:");
            civil_capa = new java.util.Scanner(System.in).nextInt();
        }
        System.out.println("人狼:"+wolf_capa+"  市民:"+civil_capa);
        System.out.println("でゲームを開始します。\n\n");
        
        //第1夜
        System.out.println("第1夜…");
        int wolf=0;
        int civil=0;
        int[] haveVote= new int[4];
        int[] life = new int[numbers];
        int[] role = new int[numbers];
        //第1夜アクション
        for( int i=0 ; i<numbers ; i++ ){
        	check(player[i]);
        	life[i] = 1;
            role[i] = new java.util.Random().nextInt(2);
	        if( role[i]==0 ){
	                civil++;
	        }else{
	            wolf++;
	        }
	        while(wolf_capa<wolf || civil_capa<civil ){
	            role[i] = new java.util.Random().nextInt(2);
	            if( 
	                wolf_capa<wolf ){
	                wolf--;
	            }else if( civil_capa<civil ){
	                civil--;
	            }
	            if( role[i]==0 ){
	                civil++;
	            }else{
	                wolf++;
	            }
	        }
	        if ( role[i]==0 ) {
	            System.out.println(player[i]+"さん、あなたは市民です。");
	        }else if ( role[i]==1 ) {
	            System.out.println(player[i]+"さん、あなたは人狼です。");
	        }
	        
	        doubt(player,numbers,haveVote);
        }
        int max = 0;
        int count1 = 0;
        int[] lastVote = new int[numbers];
    	totalization(max,lastVote,count1,false,player,numbers,haveVote);
        meeting();
        count1 = 0;
        crimevoting(player,numbers,haveVote);
        max=totalization(max,lastVote,count1,true,player,numbers,haveVote);
        execution(civil,wolf,life,max,player,numbers,lastVote);
        
        //第2夜以降
        int night = 2;
        int t;
        while ( wolf+1<civil && wolf>=1) {
        	System.out.println("第"+night+"夜…");
        	if ( night>=2 ) {
	        	System.out.println("容疑者を処刑したにも関わらず");
	        	System.out.println("再び恐ろしい夜がやってきました");
        	}
        	Arrays.fill(haveVote, 0);
        	Arrays.fill(lastVote,0);
        	t = 0;
        	for ( int i=0 ; i<numbers ; i++ ) {
        		if ( life[i]==1 ) {
        			if ( role[i]==1) {
        				t=kill(civil,t,life,player);
        			}else if ( role[i]==0 ) {
        				doubt(player,numbers,haveVote);
        			}
        		}else if ( life[i]==0 ) {
        			System.out.println("全員の役職を確認しますか？");
        			System.out.println("はい[1]  いいえ[2]");
        			int s = new java.util.Scanner(System.in).nextInt();
        			if ( s==1 ) {
        				list(player,role,numbers);
        			}
        		}
        	}
        	System.out.println("昨晩の犠牲者は"+player[t]+"さんです");
        	System.out.println(player[t]+"さんは幽霊となり、ゲーム終了まで話してはいけません");
        	totalization(max,lastVote,count1,false,player,numbers,haveVote);
        	meeting();
            count1 = 0;
            crimevoting(player,numbers,haveVote);
            max=totalization(max,lastVote,count1,true,player,numbers,haveVote);
            execution(civil,wolf,life,max,player,numbers,lastVote);
        	night++;
        }
        //ゲーム終了
        if ( wolf==0 ) {
        	System.out.println("この瞬間に、人狼は息絶えました");
        	System.out.println("この日以降、もう人狼による被害は起きませんでした");
        	System.out.println("市民チームの勝利です");
        }
        if ( wolf+1>=civil ) {
        	System.out.println("容疑者を処刑したにも関わらず");
        	System.out.println("その晩、再び人狼によって市民が殺され");
        	System.out.println("人狼と市民の数は同じになりました");
        	System.out.println("人狼チームの勝利です");
        }
    }
    
    
    
    public static void list(String[] player,int[] role,int numbers) {
    	for ( int i=0 ; i<numbers ; i++ ) {
    		System.out.print(player[i]+":");
    		if ( role[i]==0 ) {
    			System.out.println("市民");
    		}else if( role[i]==1 ) {
    			System.out.println("人狼");
    		}
    	}
    }
    
    
    
    public static int kill(int civil,int t,int[] life,String[] player) {
    	System.out.println("誰を襲いますか？");
    	t =new java.util.Scanner(System.in).nextInt();
    	life[t] = 0;
    	civil--;
    	System.out.println(player[t]+"さんを襲います");
    	return t;
    }
    
    
    //false:疑う人  true:処刑する人
    public static int totalization(int max,int[] lastVote,int count1,boolean ok,String[] player,int numbers,int[] haveVote) {
    	max = 0;
    	if ( ok==false ) {
    		System.out.println("最も疑われたのは");
    		for ( int i=0 ; i<numbers ; i++ ) {
    			max=max(max,haveVote[i]);
    		}
    		for ( int i=0 ; i<numbers ; i++) {
    			if ( haveVote[i]==max ) {
    				System.out.println(player[i]+"さん");
    			}
    		}
    		System.out.println("です\n");
    	}
    	//使う前に必ずcount1=0にリセットする
    	else if( ok==true ) {
    		count1 = 0;
		    for ( int i=0 ; i<numbers ; i++ ) {
		    	max=max(max,haveVote[i]);
		    }
			System.out.println("最も疑われたのは");
		    for ( int i=0 ; i<numbers ; i++ ) {
		    	if ( haveVote[i]==max ) {
		    		player(player,i);
			    	count1++;
			   		lastVote[i] = max;
			   	}
			   	if ( haveVote[i]!=max ) {
			    	haveVote[i] = 0;
			    }
			}
			System.out.println("です\n");
			if ( count1!=1 ) {
				System.out.println("決戦投票を行います\n");
			    max=runoff(lastVote,count1,max,player,numbers,haveVote);
		    }
    	}
    	return max;
    }
    
    
    
    public static int runoff (int[] lastVote,int count1,int max,String[] player,int numbers,int[] haveVote) {
    	int count2;
    	count1 = 0;
    	for ( int k=0 ; k<numbers ; k++ ) {
	    	if ( haveVote[k]!=max ) {
				count1 = 0;
				count2 = 1;
				check(player[k]);
				while ( count2!=0 ) {
				   	count2 = 0;
					System.out.println("誰が怪しいですか？");
				   	for ( int i=0 ; i<numbers ; i++ ) {//名前一覧
				   		if ( haveVote[i]==max ) {
				    	System.out.print(i+1);
					    	player(player,i);
				   		}
					}
				   	int j = new java.util.Scanner(System.in).nextInt()-1;
				   	if ( haveVote[j]!=max ) {
				   		System.out.println("この番号は現在無効です\nもう一度投票してください");
				   		count2++;
				   	}else {
					   	lastVote[j]++;
					   	System.out.println(player[j]+"さんを疑います\n\n");
				   	}
				}
			}
	    }
    	max = 0;
	   	for ( int i=0 ; i<numbers ; i++ ) {
	   		max=max(max,lastVote[i]);
	    }
    	return max;	
    }
    
    
    
    public static void execution (int civil,int wolf,int[] life,int max,String[] player,int numbers,int[] lastVote) {
    	System.out.println("最も疑われたのは");
	    for ( int i=0 ; i<numbers ; i++ ) {
	    	if ( lastVote[i]==max ) {
	    		player(player,i);
	    	}
	    }
	    System.out.println("です\n");
    	for ( int i=0 ; i<numbers ; i++ ) {
			if ( lastVote[i]==max ) {
    			player(player,i);
    			life[i]=0;
    			if ( i==0 ) {
    				civil--;
    			}else if( i==1 ) {
    				wolf--;
    			}
	    	}
		}
		System.out.println("を処刑します");
		for ( int i=0 ; i<numbers ; i++ ) {
			if ( lastVote[i]==max ) {
    			player(player,i);
	    	}
		}
		System.out.println("さんは幽霊となり、ゲーム終了まで話してはいけません");
    }
    
    
    
    public static int max(int max,int vote) {
    	if ( max<vote ) {
	    	max = vote;
	    }
    	return max;
    }
    
    
    
    public static void player(String[] player,int p) {
    	System.out.println(player[p]);
    }
    
    
    
    public static void doubt(String[] player,int numbers,int[] haveVote) {
    	System.out.println("誰が怪しいですか？");
        for( int j=0 ; j<numbers ; j++ ) {
        	System.out.print(j+1);
        	player(player,j);
        }
        int j = new java.util.Scanner(System.in).nextInt()-1;
		haveVote[j]++;
		System.out.println(player[j]+"さんを疑います\n\n");
	}
    
    
    
    public static void crimevoting(String[] player,int numbers,int[] haveVote) {
    	Arrays.fill(haveVote, 0);
    	for ( int i=0 ; i<numbers ; i++ ) {
    		check(player[i]);
	    	System.out.println("誰が怪しいですか？");
	        for( int j=0 ; j<numbers ; j++ ) {
	        	System.out.print(j+1);
	        	player(player,j);
	        }
	        int j = new java.util.Scanner(System.in).nextInt()-1;
			haveVote[j]++;
			System.out.println(player[j]+"さんを疑います\n\n");
    	}
		
	}
    
    
    
    public static void meeting() throws Exception{
    	System.out.println("\n話し合ってください");
    	for ( int i=1 ; i<6 ; i++ ) {
    		System.out.print(i+",");
        	Thread.sleep(1000);
    	}
    	System.out.println("");
    	System.out.println("5秒延長しますか？");
    	System.out.println("はい[1]   いいえ[2]");
    	int i = new java.util.Scanner(System.in).nextInt();
    	while ( i==1 ) {
    		System.out.println("\n話し合ってください");
    		for ( int j=1 ; j<6 ; j++ ) {
        		System.out.print(j+",");
            	Thread.sleep(1000);
        	}
    		System.out.println("");
    		System.out.println("さらに5秒延長しますか？");
        	System.out.println("はい[1]   いいえ[2]");
    		i = new java.util.Scanner(System.in).nextInt();
    	}
    }

    
    
    public static void check(String player) {
        int kakunin =2;
        System.out.println(player+"さん本人ですか？");
        System.out.println("『はい』なら1、『いいえ』なら2を入力してください。");
        kakunin = new java.util.Scanner(System.in).nextInt();
        while( kakunin==2){
            System.out.println(player+"さん本人ですか？");
            System.out.println("『はい』なら1、『いいえ』なら2を入力してください。");
            kakunin = new java.util.Scanner(System.in).nextInt();
        }
    }

    
    
}