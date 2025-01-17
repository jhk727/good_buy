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
							+ "&redirect_uri=http://c3d2407t1p2.itwillbs.com/callback"
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
	$("html").addClass("fixed");
	if(modal == 'charge') {	
		document.getElementById('pay-account-modal').style.display = 'block';
	} else if (modal == 'refund') {
		document.getElementById('pay-refund-modal').style.display = 'block';
	}
}

function closeModal(modal) {
	$("html").removeClass("fixed");
	if(modal == 'charge') {	
		document.getElementById('pay-account-modal').style.display = 'none';
	} else if (modal == 'refund') {
		document.getElementById('pay-refund-modal').style.display = 'none';
	}
}

function addAmount(amount, type) {
    var inputField;
    if(type == 'ch') {		
    	inputField = document.getElementById('total-amount-charge');
    } else if(type == 're') {
    	inputField = document.getElementById('total-amount');
	}
    const currentValue = parseInt(inputField.value) || 0;  // 현재 값 가져오기 (없으면 0)
    inputField.value = currentValue + amount;  // 금액 추가
    
    $("input[name=tran_amt]").val(inputField.value);
}





function listToggleButton(type) {
	const buttons = document.querySelectorAll('.use-buttons > button');
	// 목록 활성화 상태 업데이트
	const lists = document.querySelectorAll('.use-history-item');
	buttons.forEach(button => {
	    // 모든 버튼에서 selected 클래스 제거
	    buttons.forEach(btn => btn.classList.remove('selected'));
	    buttons.forEach(btn => {
			if (type === 'all') {
				$(".all-btn").addClass('selected');
			} else if(type === 'transfer') {
				$(".use-transfer-btn").addClass('selected');
			} else if(type === 'charge') {
				$(".use-charge-btn").addClass('selected');
			} else if(type === 'refund') {
				$(".use-refund-btn").addClass('selected');
			}
		});
	    
		 lists.forEach(list => {
            if (list.dataset.type === type || type === 'all') {
                list.classList.add('active');
            } else {
                list.classList.remove('active');
            }
		});
	});
}



function searchDate() {
	let start_date = $('#start_date').val(); 
	let end_date = $('#end_date').val();
	location.href="AllPayList?start_date=" + start_date + "&end_date=" + end_date;
}


// --------- 환불 모달에서 잔액보다 크면 환불불가 --------
$(document).ready(function(){
//.balance-info에 있는 텍스트 값을 읽어오면 되는데??
//	let maxAmount = $("#fefund-balance").text(); // 이거 안된다 왜녀면 ,를 넣어서 string
	let maxAmount = $("#pay_amount").val();
	
	// 잔액보다 높으면 경고메시지 띄움.
	const amountInput = document.getElementById('total-amount');
	
	const warningMessage = document.getElementById('warningMessage');
	
	amountInput.addEventListener('input', () => {
	    const inputValue = parseInt(amountInput.value, 10);
		console.log(maxAmount);
		console.log("입력액 : " + inputValue);
	
	    if (inputValue > maxAmount) {
	        warningMessage.style.display = 'block'; // 경고 메시지 표시
	        amountInput.value = maxAmount; // 최대 금액으로 입력 제한
	    } else {
	        warningMessage.style.display = 'none'; // 경고 메시지 숨김
	    }
	});
});