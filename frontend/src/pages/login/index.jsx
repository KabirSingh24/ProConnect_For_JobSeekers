import UserLayout from "@/layout/UserLayout";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import styles from "./style.module.css";
import { registerUser, loginUser } from "@/config/redux/action/authAction";
import { emptyMessage } from "@/config/redux/reducer/authReducer";




export default function LoginComponent() {

    const authState = useSelector((state) => state.auth)

    const router = useRouter()
    const dispatch = useDispatch();

    const [userLoginMethod, setUserLoginMethod] = useState(false)
    const [email, setEmail] = useState("");
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");

    useEffect(() => {
        if (authState.loggedIn) {
            router.push("/dashboard");
        }
    }, [authState.loggedIn])

    useEffect(() => {
        dispatch(emptyMessage())
    }, [userLoginMethod])

    useEffect(() => {
        if (localStorage.getItem("token")) {
            router.push("/dashboard")
        }
    }, [])

    useEffect(() => {
        if (authState.isSuccess && !userLoginMethod) {
            setUserLoginMethod(true);
            setEmail("");
            setPassword("");
            setUserName("");
            setName("");
            dispatch(emptyMessage());
        }
    }, [authState.isSuccess]);

    const handleRegister = () => {
        setUserLoginMethod(true)
        dispatch(registerUser({ name, username, email, password }))
    }
    const handleLogin = () => {
        dispatch(loginUser({ email, password }))
    }


    return (
        <UserLayout>
            <div className={styles.container}>
                <div className={styles.cardContainer}>
                    <div className={styles.cardContainer_left}>

                        <p className={styles.cardleft_heading}>{userLoginMethod ? "Sign In" : "Sign Up"}</p>
                        {authState.isError && <p style={{ color: "red" }}>{authState.message}</p>}
                        {authState.isSuccess && <p style={{ color: "green" }}>{authState.message}</p>}



                        <div className={styles.inputContainers}>


                            {!userLoginMethod && (
                                <div className={styles.inputRow}>
                                    <input
                                        onChange={(e) => setUserName(e.target.value)}
                                        className={styles.inputField}
                                        placeholder="Username"
                                        type="text"
                                        value={username}
                                    />
                                    <input
                                        onChange={(e) => setName(e.target.value)}
                                        className={styles.inputField}
                                        placeholder="Name"
                                        type="text"
                                        value={name}
                                    />
                                </div>
                            )}
                            <input onChange={(e) => setEmail(e.target.value)} className={styles.inputField} placeholder="Email" type="text" />
                            <input onChange={(e) => setPassword(e.target.value)} className={styles.inputField} placeholder="Password" type="password" />


                            <div onClick={() => {
                                if (userLoginMethod) {
                                    handleLogin();
                                } else {
                                    handleRegister();
                                }
                            }} className={styles.buttonWithOutline}>
                                <p>{userLoginMethod ? "Sign In" : "Sign Up"}</p>

                            </div>
                        </div>


                    </div>
                    <div className={styles.cardContainer_right}>
                        {userLoginMethod ? <p>Don't Have an Account?</p> : <p>Already Have an Account?</p>}
                        <div onClick={() => {
                            setUserLoginMethod(!userLoginMethod)

                        }} style={{ color: "black", textAlign: "center" }} className={styles.buttonWithOutline}>
                            <p>{userLoginMethod ? "Sign Up" : "Sign In"}</p>
                        </div>

                    </div>
                </div>
            </div>
        </UserLayout>
    )
}