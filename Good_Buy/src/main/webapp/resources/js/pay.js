function linkAccount() {
	// 새 창으로 사용자 인증 요청 수행
	// => 빈 창을 먼저 띄운 후 해당 창에 사용자 인증 페이지 요청
	
	// response_type - 고정값: code Y OAuth 2.0 인증 요청 시 반환되는 형태
	// client_id : 오픈뱅킹에서 발급한 이용기관 앱의 Client ID
	// scope : 권한범위
	// state : 32 bytes 고정 - 보안위협에 대응하기 위해 이용기관이 세팅하는 난수값 (Callback 호출 시 그대로 전달됨)
	// auth_type : 사용자인증타입 구분 (0:최초인증, 1:재인증, 2:인증생략)
	let authWindow = window.open("about:blank", "authWindow", "width=500,height=700");
	authWindow.location = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?"
							+ "response_type=code" 
							+ "&client_id=4066d795-aa6e-4720-9383-931d1f60d1a9" 
							+ "&redirect_uri=http://localhost:8081/Callback"
							+ "&scope=login inquiry transfer" 
							+ "&state=12345678901234567890123456789012" 
							+ "&auth_type=0"; 
}

function submitForm(anchor) {
    // a 태그의 data-form-id 속성에서 관련 form의 ID를 가져옴
    const formId = anchor.getAttribute('data-form-id');
    const form = document.getElementById(formId);

    if (form && form.tagName === 'FORM') {
        form.submit();
    } else {
        console.error(`No <form> found with ID: ${formId}`);
    }
}

function openModal(modal) {
	if(modal == 'charge') {	
		document.getElementById('pay-account-modal').style.display = 'block';
	} else if (modal == 'refund') {
		document.getElementById('pay-refund-modal').style.display = 'block';
	}
}

function closeModal(modal) {
	if(modal == 'charge') {	
		document.getElementById('pay-account-modal').style.display = 'none';
	} else if (modal == 'refund') {
		document.getElementById('pay-refund-modal').style.display = 'none';
	}
}

function addAmount(amount) {
	console.log(amount);
    const inputField = document.getElementById('total-amount');
    const currentValue = parseInt(inputField.value) || 0;  // 현재 값 가져오기 (없으면 0)
    inputField.value = currentValue + amount;  // 금액 추가
}