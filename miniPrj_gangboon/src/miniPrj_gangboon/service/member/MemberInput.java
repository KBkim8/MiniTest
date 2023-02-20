package miniPrj_gangboon.service.member;

import miniPrj_gangboon.main.Main;

public class MemberInput {

	// 로그인 입력받기
		public MemberData login() {
			System.out.print("아이디 : ");
			String memberId = Main.SC.nextLine();
			System.out.print("패스워드 : ");
			String memberPwd = Main.SC.nextLine();
			
			MemberData data = new MemberData();
			data.setMemberId(memberId);
			data.setMemberPwd(memberPwd);
			
			return data;
			
		}
	
	
}
