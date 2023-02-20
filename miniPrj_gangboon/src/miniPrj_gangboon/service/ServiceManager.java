package miniPrj_gangboon.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

import miniPrj_gangboon.jdbc.JdbcTemplate;
import miniPrj_gangboon.main.Main;
import miniPrj_gangboon.service.member.MemberData;
import miniPrj_gangboon.service.member.MemberInput;
import miniPrj_gangboon.service.time.TimeData;

public class ServiceManager {
	
	static MemberInput mi = new MemberInput();
	public static MemberData data = mi.login();
	static int memNum = memberNum(); 
	Scanner sc2 = new Scanner(System.in);
	
	// 로그인
	public void login() throws Exception {
		// select
		Connection conn = JdbcTemplate.getConnection();
		String sql = "SELECT MEM_ID,MEM_PWD,MEM_NAME FROM MEMBER WHERE MEM_ID = ? AND MEM_PWD = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, data.getMemberId());
		pstmt.setString(2, data.getMemberPwd());
		ResultSet rs = pstmt.executeQuery();
		
		// 결과집합에서 데이터 꺼내기
		if(rs.next()) {
			String NAME = rs.getString("MEM_NAME");
			System.out.println(NAME + "님 환영합니다.");
			showMenu();
		} else {
			System.out.println("로그인 실패..");
			return;
		}
		// conn 정리
		conn.close();
	}
	
	//회원고유번호 변수 지정
	public static int memberNum() {
		Connection conn;
		try {
			conn = JdbcTemplate.getConnection();
			String sql = "SELECT MEM_NUM FROM MEMBER WHERE MEM_ID = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, data.getMemberId());
			ResultSet rs = pstmt.executeQuery();
			
			// 결과집합에서 데이터 꺼내기
			if(rs.next()) {
				int MEM_NUM = rs.getInt("MEM_NUM");
				conn.close();
				return MEM_NUM;
				}
				else {
					System.out.println("맞는 회원번호가 없습니다");
					return 0;
				}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}
		
		
		public void showMenu() throws Exception {
			System.out.println("====================================  회원메뉴 >   ====================================================");
			System.out.println("                                                                                       "); 
			System.out.println("    ┌───────────┐  ┌───────────┐  ┌─────────┐  ");
			System.out.println("    │1. 요금 선택 │  │2. 좌석 선택 │  │ 3. 종료  │  ");
			System.out.println("    └───────────┘  └───────────┘  └─────────┘   ");
			System.out.println("=====================================================================================================");
			System.out.print("메뉴를 선택하세요. > ");
			
			int input = Main.SC.nextInt();
			while(true) {
				switch(input) {
				case 1 : 
					fee(); break;
				case 2 : 
					chooseSeat(); break;
				case 3 : return;
				default : System.out.println("다시 선택하세요");
				}
			}
		}
		
		// 요금 선택
		public void fee() {
			System.out.println("나중에 예린님거 추가~~");
		};
		
		public void chooseSeat() throws Exception {
		    // 적립 시간 조회
		    String sql = "SELECT MEM_TIME FROM MEMBER WHERE MEM_ID = ?";
		    Connection conn = JdbcTemplate.getConnection();
		    PreparedStatement pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, data.getMemberId());
		    ResultSet rs = pstmt.executeQuery();
		    
		    // 결과 집합에서 데이터 꺼내서 조건문
		    if (rs.next()) {
		        int memTime = rs.getInt("MEM_TIME");
		        if (memTime == 0) {
		        	System.out.println("남은 시간이 없습니다.");
		        	System.out.println("결제를 먼저 해주세요");
		        	showMenu();
		        } else if(memTime != 0) {
		        	showSeat();
		        }
		    } else {
		        System.out.println("해당 회원 정보를 찾을 수 없습니다.");
		    }
		}
			
		// 좌석 배열 보여주기 (select 좌석 정보)
		public void showSeat() {
		    // SQL 실행
		    String sql = "SELECT SEAT_NUM, SEAT_TYPE, USAGE_YN FROM SEAT";
		    Connection conn;
		    try {
		        conn = JdbcTemplate.getConnection();
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        ResultSet rs = pstmt.executeQuery();

		        // 결과집합에서 데이터 꺼내기
		        while (rs.next()) {
		            int SEAT_NUM = rs.getInt("SEAT_NUM");
		            String SEAT_TYPE = rs.getString("SEAT_TYPE");
		            String USAGE_YN = rs.getString("USAGE_YN");

		            System.out.print(SEAT_NUM + "번" + " ◻︎ ");
		            System.out.print(" || ");

		            if (SEAT_TYPE.equals("금연")) {
		                System.out.print(" 금연 좌석 ");
		            } else {
		                System.out.print(" 흡연 좌석 ");
		            }

		            System.out.print(" || ");

		            if (USAGE_YN.equals("Y")) {
		                System.out.println("사용 중 ... ");
		            } else {
		                System.out.println("사용 가능");
		            }
		        }
		        System.out.println();
		        System.out.print("좌석을 선택하세요. >>>>>>>  ");
		        // 좌석 사용 여부 메소드 불러오기
		        usage_YN();
		        // conn 정리
		        conn.close();
		    } catch (Exception e) {
		    	System.err.println("Error: " + e.getMessage());
		    }
		}

		//좌석 사용여부
		public void usage_YN() {
		    int input = Main.SC.nextInt();
		    // 저장된 좌석 번호로 사용 여부 조회
		    String sql1 = "SELECT SEAT_NUM, USAGE_YN FROM SEAT WHERE SEAT_NUM = ?";
		    // 좌석 선택과 동시에 이용내역 INSERT
		    String sql2 = "INSERT INTO PC_USE(USE_NUM,SEAT_NUM,MEM_NUM, PC_STARTTIME) VALUES(PC_USE_SEQ.NEXTVAL,?,?,SYSDATE)";
		    // 좌석 선택 성공 시 멤버테이블 사용 여부 업데이트
		    String sql3 = "UPDATE SEAT SET USAGE_YN = 'Y' WHERE SEAT_NUM = ?";
		    Connection conn;
		    try {
		        conn = JdbcTemplate.getConnection();
		        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
		        pstmt1.setInt(1, input);
		        ResultSet rs = pstmt1.executeQuery();
		        
		        // 결과 꺼내서 사용 여부 알려주기
		        rs.next(); 
		            String USAGE_YN = rs.getString("USAGE_YN");
		            int SEAT_NUM = rs.getInt("SEAT_NUM");
		            if (USAGE_YN.equals("Y")) {
		                System.out.println("이미 사용 중인 좌석입니다. ");
		                showSeat();
		            } else {
		                System.out.println(SEAT_NUM + "번 좌석을 선택하셨습니다.");
		        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
		        pstmt2.setInt(1,SEAT_NUM);
		        pstmt2.setInt(2,memNum);
		        int result1 = pstmt2.executeUpdate();
		        
		        //좌선 선택함과 동시에 좌석테이블 사용 여부 업데이트
		        PreparedStatement pstmt3 = conn.prepareStatement(sql3);
		        pstmt3.setInt(1,SEAT_NUM);
		        int result2 = pstmt3.executeUpdate();
        		        if(result2 ==1) {
        		        	System.out.println("이용이 시작되었습니다.");
        		        	afterChooseSeat();
        		        } else {
        		        	System.out.println("이용 실패");
        			    }
		            }
		          
		        // conn 정리
		        conn.close();
		    } catch (Exception e) {
		    	System.err.println("Error: " + e.getMessage());
		    }
		}
		
		// 좌석 선택 시 나오는 화면
		public void afterChooseSeat() {
			
			System.out.println("====================================  회원메뉴 >   ====================================================");
			System.out.println("                                                                                       "); 
			System.out.println("    ┌───────────┐  ┌───────────────┐  ┌─────────────┐  ┌─────────────┐  ");
			System.out.println("    │1. 상품 주문 │  │2. 남은 시간 조회 │  │ 3. 추가 결제  │  │ 4. 이용 종료  │  ");
			System.out.println("    └───────────┘  └───────────────┘  └─────────────┘  └─────────────┘ ");
			System.out.println("=====================================================================================================");
			System.out.print("번호를 선택하세요. >>>>>>>> ");
			
			int input = Main.SC.nextInt();
			switch(input){
			case 1 : orderFood(); break;
			case 2 : showRemainTime(); break;
			case 3 : showTimeTable(); break;
			case 4 : stopUse(); break;
			default : System.out.println("다시 선택하세요");
			}
		}
		
		public void orderFood() {
			
		System.out.println("지웅님 파트 끌고오기~~");
		
		}
		
		//남은 시간 조회 메소드
		public void showRemainTime() {
			Connection conn;
			try {
				conn = JdbcTemplate.getConnection();
				String sql3 = "SELECT USE_NUM FROM PC_USE WHERE MEM_NUM = ? ORDER BY USE_NUM DESC ";
				
				PreparedStatement pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setInt(1, memNum);
				ResultSet rs3 = pstmt3.executeQuery();
				rs3.next();
				//가장 최신의 use_num을 가져와서 저장
				int useNum = rs3.getInt("USE_NUM");
				
				// 사용 좌석으로 시작시간 조회
				String sql1 ="SELECT TO_CHAR(PC_STARTTIME,'DD HH24:MI:SS') AS S_TIME FROM PC_USE WHERE USE_NUM = ?";
				// 회원번호로 이름, 적립 시간 조회
				String sql2 = "SELECT MEM_NAME,MEM_TIME FROM MEMBER WHERE MEM_NUM = ?";
				
				PreparedStatement pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setInt(1, useNum);
				ResultSet rs1 = pstmt1.executeQuery();
				rs1.next();
					String startTime=rs1.getString("S_TIME");
					
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd HH:mm:ss");
					//현재 시간
					Date now = new Date();
					String nowTime = sdf1.format(now);
					Date nowT = sdf1.parse(nowTime);
					Date startDate = sdf1.parse(startTime);
					
					long time1 = startDate.getTime();
					long time2 = now.getTime();
					
					// 현재 시간 - 시작 시간 = 이용 시간
					long diff = time2-time1;
					long diffMin = (diff/1000)/60%60;
					
					int diffMin2 = (int)diffMin;
					
				//적립시간 찾아와서 적립시간 - 이용 시간 = 현재 남은 시간
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, memNum);
				ResultSet rs2 = pstmt2.executeQuery();
				rs2.next();
					int memTime = rs2.getInt("MEM_TIME");
					String memName = rs2.getString("MEM_NAME");
					int rtime = memTime - diffMin2;
					
					System.out.println(memName + "님의 남은 시간은 " + rtime + "분 입니다.");
					afterChooseSeat();
				
				// conn 정리
		        conn.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		
		// 추가결제
		// 현재 시간-시작시간 = 현재까지 이용한 시간
		// 새로 결제한 시간권 시간 + 현재까지 이용한 시간 => 적립시간에 업데이트
		public void showTimeTable(){
			try {
				Connection conn = JdbcTemplate.getConnection();
				System.out.println("=========피시방 요금===========");
			
				String sql = "SELECT * FROM TIME";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				//시간권 목록 보여주기
				while(rs.next()) {
					System.out.println(rs.getString("T_NUM")+".가격:"+rs.getString("T_PRICE")+"원,시간:"+rs.getString("T_TIME")+"분");
				}
				System.out.println("9.이전화면으로 돌아가기");
				
				//클로즈
				conn.close();
				inputFee();
				
			}catch(SQLException se) {
				System.out.println("DB에러 발생. DB관리자에 문의하세요.");
				System.out.println("오류상세 정보"+se.toString());
			}catch(Exception e) {
				System.out.println("커넥션 오류 발생.");
				System.out.println(e.toString());
			}
	}
		
		//요금제 입력받기
		//입력받은 요금제로 데이터 변수에 저장하기
		//feeNum: 사용자로부터 입력받은 요금제 고유번호
		//timeAddMin: 요금제 번호에 맞는 요금제 시간, 추가시간같은 개념
		//timePrice: 요금제 번호에 맞는 요금제 가격
		//timeMin: 	회원테이블에서 받아온 적립시간
		public void inputFee() {
			try {
				TimeData data = new TimeData();
				System.out.print("요금제를 선택하세요.:");
				int feeInput = Main.SC.nextInt();
				data.setFeeNum(feeInput);
					
				//feeNum을 받는 동시에 timeAddMin구하기 가능함
				//timeMin도 당장 받아올수있음. 세게의 변수를 한꺼번에 받아서 data 로 묶자
			
				Connection conn = JdbcTemplate.getConnection();
				//timeAddMin구하기 : 요금 결제시 고객이 선택한 추가시간임.
				String sql1 = "SELECT T_TIME, T_PRICE FROM TIME WHERE T_NUM = ?";
				PreparedStatement pstmt1 = conn.prepareStatement(sql1); //se
				pstmt1.setInt(1,data.getFeeNum()); //se
				ResultSet rs = pstmt1.executeQuery(); //se
				rs.next(); //se
				data.setTimeAddMin(rs.getInt("T_TIME"));//se
				//int timeAddMin = rs.getInt("T_TIME");
				data.setTimePrice(rs.getInt("T_PRICE")); //se
				//int timePrice = rs.getInt("T_PRICE");
		
				//timeMin받아오기 : 회원테이블의 남은 시간
				String sql2 = "SELECT MEM_TIME FROM MEMBER WHERE MEM_NUM= ?";
				PreparedStatement pstmt2 = conn.prepareStatement(sql2); //se
				pstmt2.setInt(1, memNum); //se
				ResultSet rs2 = pstmt2.executeQuery(); //se
				if(rs2.next()) { //se
					data.setTimeMin(rs2.getInt("MEM_TIME")); //se
				//int timeMin = rs2.getInt("MEM_TIME");
				}
					
				conn.close(); //se
				//다음단계로 넘어가기
				getInfo(data);
			}catch(SQLException se) {
				System.out.println("값이 잘못 입력되었는지 확인해주세요.");
				System.out.println("상세오류를 확인합니다. 관리자에게 보여주세요.");
				System.out.println(se.toString());
				System.out.println("이전 단계로 돌아갑니다.");
				showTimeTable();
				
			}catch(Exception e) {
				System.out.println("알수없는 오류. 관리자를 호출하세요.");
				System.out.println("상세오류를 확인합니다. 관리자에게 보여주세요.");
				System.out.println(e.toString());
				System.out.println("이전단계로 돌아갑니다.");
				//이전단계
				showTimeTable();
			}
			
			//timeAddMin과timeMin과feeNum을 한꺼번에 데이터로 관리하기
		}	
		
		//100분, 2000원 결제 하시겠습니까? 결제안전장치기능
		public void getInfo(TimeData data) {
			System.out.print(data.getTimeAddMin()+"분, "+data.getTimePrice()+"원 결제 하시겠습니까? (y/n)");
			String input = sc2.nextLine().trim();
			if(input.equalsIgnoreCase("y")) {
				System.out.println("결제가 완료되었습니다.");
				addPayList(data);
			}else if(input.equalsIgnoreCase("n")) {
				System.out.println("결제가 취소되었습니다. 이전 화면으로 돌아갑니다.");
				showTimeTable();
				//뒤로가기하면 시간권선택화면으로 돌아가기 
			}else {
				System.out.println("잘못입력하셨습니다. 이전 화면으로 돌아갑니다.");
				showTimeTable();
			}
			
		}
		
		//시간권 결제내역테이블에 결제 내역 추가하기
		public void addPayList(TimeData data) {
			try {
			//커넥션 받아오기
			Connection conn = JdbcTemplate.getConnection(); //Exception e 
			
			//SQL문작성 : 결제내역 인서트 
			//feeNum은 static 멤버변수로 선언하여 어디서든 이용가능하게끔 
			String sql= "INSERT INTO TIME_PAYMENT VALUES (TP_NUM.NEXTVAL,?,SYSDATE,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql); //SQLException e
			
			pstmt.setInt(1, data.getFeeNum()); //SQLException e
			
			pstmt.setInt(2, memNum); //SQLException e
			
			int result = pstmt.executeUpdate(); //SQLException e
			
			if(result ==1) {
				updateTimeDate(data);
			}else {
				System.out.println("결제 실패. 전단계로 돌아갑니다.");
				showTimeTable();
				//돌아가는 코드 작성해야함.
			}
			
			//커넥션 종료
			conn.close(); //SQLException e
			}catch(SQLException se) {
				System.out.println("DB문제입니다. 이전 단계로 돌아갑니다.");
			}catch(Exception e) {
				System.out.println("커넥션오류입니다.");
			}finally { //오류와 상관없이 무조건 실행되는 구
				
			}
			
		}
		//추가시간 적립하기 
		//memNum이라는 값을 받아오게 만들었음. 
		public void updateTimeDate(TimeData data){
			Connection conn;
			try{
				conn = JdbcTemplate.getConnection();//exception
				String sql3 = "SELECT USE_NUM FROM PC_USE WHERE MEM_NUM = ? ORDER BY USE_NUM DESC ";
				
				PreparedStatement pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setInt(1, memNum);
				ResultSet rs3 = pstmt3.executeQuery();
				rs3.next();
				//가장 최신의 use_num을 가져와서 저장
				int useNum = rs3.getInt("USE_NUM");
				
				//SQL문 작성(update) : 적립시간에 결제시간 더한 값 회원정보에 업데이트하기 
				//(시간권 결제한 시간 + (적립시간-(현재시간-사용시작시간 = 현재까지 이용한 시간)))
				String sql1 = "SELECT TO_CHAR(PC_STARTTIME,'DD HH24:MI:SS') AS S_TIME FROM PC_USE WHERE USE_NUM = ?";
				String sql2 = "SELECT MEM_TIME FROM MEMBER WHERE MEM_NUM = ?";
				String sql4 = "UPDATE MEMBER SET MEM_TIME = ? WHERE MEM_NUM = ?";
				PreparedStatement pstmt1 = conn.prepareStatement(sql1); 
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				PreparedStatement pstmt4 = conn.prepareStatement(sql4);
				pstmt1.setInt(1, useNum);
				ResultSet rs1 = pstmt1.executeQuery();
				rs1.next();
					String startTime=rs1.getString("S_TIME");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd HH:mm:ss");
					//현재 시간- 시작 시간 = 지금까지 이용한 시간
					Date now = new Date();
					String nowTime = sdf1.format(now);
					Date nowT = sdf1.parse(nowTime);
					Date startDate = sdf1.parse(startTime);
					String startT = sdf1.format(startDate);
					
					long time1 = startDate.getTime();
					long time2 = now.getTime();
					
					long diff = time2-time1;
					long diffMin = (diff/1000)/60%60;
					int diffMin2 = (int)diffMin;
					
					
					//적립시간 찾아와서 적립시간 - 이용 시간 = 현재 남은 시간
					pstmt2.setInt(1, memNum);
					ResultSet rs2 = pstmt2.executeQuery();
					rs2.next();
					int memTime = rs2.getInt("MEM_TIME");
					int rtime = memTime - diffMin2;
					int totalT = data.getTimeAddMin()+rtime;
					
					//적립 시간 업데이트
					pstmt4.setInt(1, totalT);
					pstmt4.setInt(2, memNum);
					int result = pstmt4.executeUpdate();
					if(result == 1) {
						System.out.println(data.getTimeAddMin() +"분이 추가되었습니다.");
						System.out.println("남은 시간 : " + totalT);
						afterChooseSeat();
					}else {
						System.out.println("오류발생. 전단계로 돌아갑니다.");
						showTimeTable();
					}
				conn.close();
				}catch(Exception e) {
					System.out.println("커넥션오류발생. 전단계로 돌아갑니다.");
					System.out.println(e.toString());
					showTimeTable();
				}
		}
			
		//이용 종료
		public void stopUse() {
			System.out.println("종료하시겠습니까?");
			System.out.println("1. 네       2. 아니오");
			int input = Main.SC.nextInt();
			switch(input) {
			case 1 : calRemainTime(); break;
			case 2 : afterChooseSeat(); break;
			default : System.out.println("다시 선택하세요");
			}
			
		}
		public void calRemainTime() {
			Connection conn; //커넥션받기
			try {
				conn = JdbcTemplate.getConnection();
				//END_TIME UPDAEE하기 이용 종료시간 업데이트
				//정확히 어떤 줄에 업데이트해줄건지. 
				//결과집합에서 첫째줄의 USE_NUM가져와서 
				String sql4 = "SELECT USE_NUM FROM PC_USE WHERE MEM_NUM = ? ORDER BY USE_NUM DESC ";
				
				PreparedStatement pstmt4 = conn.prepareStatement(sql4);
				pstmt4.setInt(1, memNum);
				ResultSet rs4 = pstmt4.executeQuery();
				rs4.next();
				//가장 최신의 use_num을 가져와서 저장
				int useNum = rs4.getInt("USE_NUM");
				
				System.out.println(useNum);
				
				//useNum에 해당하는 줄의 종료시간을 업데이트
				String sql5 = "UPDATE PC_USE SET PC_ENDTIME = SYSDATE WHERE USE_NUM = ?";
				PreparedStatement pstmt5 = conn.prepareStatement(sql5);
				pstmt5.setInt(1, useNum);
				int result5 = pstmt5.executeUpdate();
				if(result5==1) {
					System.out.println("종료되었습니다.");
				}else {
					System.out.println("DB오류입니다.");
				}
				
				//시작시간, 종료시간 조회
				String sql1 ="SELECT TO_CHAR(PC_STARTTIME,'DD HH24:MI:SS') AS S_TIME, TO_CHAR(PC_ENDTIME,'DD HH24:MI:SS')AS E_TIME FROM PC_USE WHERE USE_NUM = ?";
				//회원번호에 맞는 회원의 적립시간 조회
				String sql2 = "SELECT MEM_TIME FROM MEMBER WHERE MEM_NUM = ?"; 
				//회원번호에 맞는 회원의 적립시간을 바꿔줌
				String sql3 = "UPDATE MEMBER SET MEM_TIME = ? WHERE MEM_NUM = ?";
				
				PreparedStatement pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setInt(1,useNum);
				ResultSet rs1 = pstmt1.executeQuery();
				rs1.next();
				String startTime=rs1.getString("S_TIME");
				String endTime=rs1.getString("E_TIME");
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd HH:mm:ss");
				
				Date startDate = sdf1.parse(startTime);
				Date endDate =sdf1.parse(endTime);
				
				long time1 = startDate.getTime();
				long time2 = endDate.getTime();
				
				long diff = time2-time1;
				
				long diffMin = (diff/1000)/60%60;
				
				int diffMin2 = (int)diffMin;
				
				// 적립 시간 꺼내와서 적립시간 - DIFFMIN
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1,memNum); 
				ResultSet rs2 = pstmt2.executeQuery();
				rs2.next();
				int memTime = rs2.getInt("MEM_TIME");
				int rtime = memTime - diffMin2;
				
				//남은 시간을 회원 테이블 적립시간에 업데이트
				PreparedStatement pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setInt(1,rtime); 
				pstmt3.setInt(2,memNum); 
				int result = pstmt3.executeUpdate();
				if(result == 1) {
					System.out.println("--------------------------------");
					System.out.println("남은 시간은 " + rtime + "분 입니다.");
					System.out.println("안녕히 가세요.");
					System.out.println("--------------------------------");
				}else {
					System.out.println("오류발생.");
				}
		
				conn.close();
			} catch (Exception e) {
		    	System.err.println("Error: " + e.getMessage());
			}
		}
	
}

	


	
	
