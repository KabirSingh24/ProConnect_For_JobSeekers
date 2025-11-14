import { AcceptConnection, getMyConnectionRequest } from "@/config/redux/action/authAction";
import DashBoardLayout from "@/layout/DashBoardLayout"
import UserLayout from "@/layout/UserLayout"
import React, { useEffect } from "react"
import { connect, useDispatch, useSelector } from "react-redux"
import styles from "./styles.module.css"
import { useRouter } from "next/router";

export default function MyConnection() {

    const dispatch = useDispatch();
    const authState = useSelector((state) => state.auth);
    const router = useRouter();
    useEffect(() => {
        dispatch(getMyConnectionRequest(localStorage.getItem("token")));
    }, [])
    useEffect(() => {
        if (authState.connectionRequest.length != 0) {
            console.log(authState.connectionRequest)
        }

    }, [authState.connectionRequest])
    return (
        <UserLayout>
            <DashBoardLayout>
                <div style={{ display: "flex", flexDirection: "column", gap: "1.7rem" }}>
                    <h4>My Connections</h4>
                    {authState.connectionRequest.length == 0 && <h1>No Connections Request Yet</h1>}
                    <div>
                        {
                            authState.connectionRequest.length != 0 && authState.connectionRequest.filter((connection) => connection.accepted === "PENDING").map((user, index) => {
                                return (
                                    <div onClick={() => {
                                        router.push(`/view_profile/${user.username}`)
                                    }} key={index} className={styles.userCard}>
                                        <div style={{ display: "flex", alignItems: "center", gap: "1.2rem" }}>
                                            <div className={styles.profilePicture}>
                                                <img src={`${user.profilePicture
                                                    }`} />
                                            </div>
                                            <div className={styles.userInfo}>
                                                <h1>{user.name}</h1>
                                                <p>{user.username}</p>
                                            </div>
                                            <button onClick={(e) => {
                                                e.stopPropagation();
                                                dispatch(AcceptConnection({
                                                    requestId: user.id,
                                                    token: localStorage.getItem("token"),
                                                    action: "ACCEPTED"
                                                }))
                                            }} className={styles.connectedButton}>Accept</button>
                                        </div>
                                    </div>
                                )
                            })
                        }

                        <h4>My Networks</h4>
                        {authState.connectionRequest.filter((connection) => connection.accepted != "PENDING").map((user, index) => {
                            return (
                                <div onClick={() => {
                                    router.push(`/view_profile/${user.username}`)
                                }} key={index} className={styles.userCard}>
                                    <div style={{ display: "flex", alignItems: "center", gap: "1.2rem" }}>
                                        <div className={styles.profilePicture}>
                                            <img src={`${user.profilePicture
                                                }`} />
                                        </div>
                                        <div className={styles.userInfo}>
                                            <h1>{user.name}</h1>
                                            <p>{user.username}</p>
                                        </div>
                                    </div>
                                </div>
                            )

                        })}
                    </div>
                </div>

            </DashBoardLayout>
        </UserLayout>

    )
}