package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InterCommand {
	
	// 자동적으로 public abstract 가 빠져있음 다른곳에서 재정의를 해야 한다. 지금은 부모 클래스인 abstractController 로 간다.
	void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
	// 무조건 이것은 필요하다. (무조건 실행을 해야 하기 때문이다.)
	
}
