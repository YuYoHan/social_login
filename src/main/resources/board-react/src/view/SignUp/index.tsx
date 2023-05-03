import axios from "axios";
import React, { useState } from "react";

export default function SignUp() {
    const [requestResult, setRequestResult] = useState<string>("");

    const signUpHandler = () => {
        const data = {
            userEmail: "test@naver.com",
            userPassword: "zxzz12",
            userPasswordCheck: "zxzz12",
            userNickName: "tester",
            userPhoneNumber: "010-1111-2222",
            userAddress: "대한민국 서울시",
            userAddressDetail: "서초구",
        };
        axios
            .post("/api/auth/signUp", data)
            .then((response) => {
                setRequestResult("Success!!");
            })
            .catch((error) => {
                setRequestResult("failed!!");
            });
    };

    return (
        <div>
            <h3>{requestResult}</h3>
            <button onClick={() => signUpHandler()}>회원가입</button>
        </div>
    );
}
