import { clientServer } from "@/config";
import DashBoardLayout from "@/layout/DashBoardLayout";
import UserLayout from "@/layout/UserLayout";
import { useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";
import styles from "./styles.module.css"
import { useRouter } from "next/router";
import { useDispatch, useSelector } from "react-redux";
import { getAllPosts } from "@/config/redux/action/postAction";
import { sendConnectionRequest, getConnectionsRequest, getMyConnectionRequest } from "@/config/redux/action/authAction";

export default function ViewProfilePage({ userProfile }) {
    const searchParams = useSearchParams();
    const router = useRouter();
    const postState = useSelector((state) => state.posts)
    const dispatch = useDispatch();
    const authState = useSelector((state) => state.auth)
    const [userPosts, setUserPosts] = useState([]);
    const [isCurrentUserInConnection, setIsCurrentUserInConnection] = useState(false)
    const [isConnectionNull, setIsConnectionNull] = useState(true)

    const getUserPost = async () => {
        await dispatch(getAllPosts());
        await dispatch(getConnectionsRequest(localStorage.getItem("token")))
        await dispatch(getMyConnectionRequest(localStorage.getItem("token")))

    }

    useEffect(() => {
        let post = postState.posts.filter((post) => {
            return post.user.username === router.query.username
        })
        setUserPosts(post)
    }, [postState.posts])


    useEffect(() => {
        console.log(authState.connections, userProfile.id)
        if (authState.connections.some(user => user.connectionId === userProfile.id)) {
            setIsCurrentUserInConnection(true);
            if (authState.connections.find(user => user.connectionId === userProfile.id)?.accepted === "ACCEPTED") {
                setIsConnectionNull(false);
            }
        }
        if (authState.connectionRequest.some(user => user.userId === userProfile.id)) {
            setIsCurrentUserInConnection(true);
            if (authState.connectionRequest.find(user => user.userId === userProfile.id)?.accepted === "ACCEPTED") {
                setIsConnectionNull(false);
            }
        }

    }, [authState.connections, authState.connectionRequest])

    useEffect(() => {
        getUserPost();
    }, [])



    useEffect(() => {
        console.log("View Profile")
    }, []);
    return (
        <UserLayout>
            <DashBoardLayout>
                <div className={styles.container}>
                    <div className={styles.backdropContainer}>
                        <img className={styles.backdrop} src={userProfile.profilePicture} alt="ProfilePicture" />
                    </div>
                    <div className={styles.profileContainer_details}>
                        <div className={styles.profileContainer_flex}>

                            <div style={{ flex: "0.8" }}>


                                <div style={{ display: "flex", width: "fit-content", alignItems: "center", gap: "1.2rem" }}>
                                    <h2>{userProfile.name}</h2>
                                    <p style={{ color: "grey" }}>{userProfile.username}</p>
                                </div>


                                <div style={{ display: "flex", alignItems: "center", gap: "1.2rem" }}>
                                    {isCurrentUserInConnection ? <button className={styles.connectedButton}>{isConnectionNull ? "Pending" : "Connected"}
                                    </button> : <button
                                        onClick={() => {
                                            if (authState.user?.id === userProfile?.id) {
                                                alert("You cannot connect with yourself");
                                                return;
                                            }
                                            dispatch(sendConnectionRequest({
                                                token: localStorage.getItem("token"),
                                                userId: userProfile.id
                                            }))
                                        }} className={styles.connectBtn}>connect
                                    </button>
                                    }
                                    <div onClick={async () => {
                                        try {
                                            const response = await clientServer.get((`/user/${userProfile.id}/download_resume`), {
                                                headers: {
                                                    Authorization: `Bearer ${localStorage.getItem("token")}`
                                                },
                                                responseType: "blob"
                                            })
                                            const fileURL = window.URL.createObjectURL(
                                                new Blob([response.data], { type: "application/pdf" })
                                            );
                                            window.open(fileURL);
                                        } catch (error) {
                                            console.error("Failed to download resume:", error);
                                            alert("Error downloading resume");
                                        }
                                    }} style={{ cursor: "pointer" }}>
                                        <svg style={{ width: "1.3em" }} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6">
                                            <path strokeLinecap="round" strokeLinejoin="round" d="M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5M16.5 12 12 16.5m0 0L7.5 12m4.5 4.5V3" />
                                        </svg>

                                    </div>
                                </div>
                                <div>
                                    <p>{userProfile.bio}</p>
                                </div>

                            </div>






                            <div style={{ flex: "0.2" }}>
                                <h3>Recent Activity</h3>
                                {userPosts.map((post) => {
                                    return (
                                        <div key={post._id} className={styles.postCard}>
                                            <div className={styles.card}>
                                                <div className={styles.card_profileContainer}>
                                                    {post.fileUrl !== "" ? <img src={`${post.fileUrl}`} alt="Posts" />
                                                        : <div style={{ width: "3.4rem", height: "3.4rem" }}></div>}

                                                </div>
                                                <p>{post.body}</p>
                                            </div>

                                        </div>
                                    )
                                })}
                            </div>
                        </div>


                    </div>


                    <div className="workHistory">
                        <h4>Education</h4>
                        <div className={styles.workHistoryContainer}>
                            {
                                userProfile.education.map((edu, index) => {
                                    return (
                                        <div key={index} className={styles.workHistoryCard}>
                                            <p style={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: "0.8rem" }}>
                                                {edu.degree}-{edu.school}
                                            </p>
                                            <p>{edu.fieldOfStudy}</p>

                                        </div>
                                    )
                                })
                            }

                        </div>
                    </div>


                    <div className="workHistory">
                        <h4>Work History</h4>
                        <div className={styles.workHistoryContainer}>
                            {
                                userProfile.pastWork.map((work, index) => {
                                    return (
                                        <div key={index} className={styles.workHistoryCard}>
                                            <p style={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: "0.8rem" }}>
                                                {work.company}-{work.postion}
                                            </p>
                                            <p>{work.year}</p>

                                        </div>
                                    )
                                })
                            }

                        </div>
                    </div>



                </div>
            </DashBoardLayout>
        </UserLayout>
    )
}

export async function getServerSideProps(context) {
    try {

        const request = await clientServer.get("/user/get_profile_based_on_username", {
            params: {
                username: context.query.username,
            }
        });

        return {
            props: {
                userProfile: request.data,
            },
        };
    } catch (error) {
        console.error("Error fetching user profile:", error.message);
        return {
            notFound: true,
        };
    }
}
