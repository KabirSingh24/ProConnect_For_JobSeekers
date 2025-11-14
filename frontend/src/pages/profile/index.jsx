import { getAboutUser } from "@/config/redux/action/authAction";
import DashBoardLayout from "@/layout/DashBoardLayout";
import UserLayout from "@/layout/UserLayout";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import styles from "./styles.module.css"
import { getAllPosts } from "@/config/redux/action/postAction";
import { clientServer } from "@/config";



export default function ProfilePage() {

    const dispatch = useDispatch();

    const authState = useSelector((state) => state.auth);
    const postState = useSelector((state) => state.posts)
    const [userProfile, setUserProfile] = useState({});
    const [userPosts, setUserPosts] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isEduModalOpen, setIsEduModalOpen] = useState(false);
    const [inputData, setInputData] = useState({ company: "", position: "", year: "" })
    const [eduInputData, setEduInputData] = useState({ school: "", degree: "", fieldOfStudy: "" });


    const handleWorkInputChange = (e) => {
        const { name, value } = e.target
        setInputData({ ...inputData, [name]: value })

    }

    const handleEduInputChange = (e) => {
        const { name, value } = e.target;
        setEduInputData({ ...eduInputData, [name]: value });
    };

    useEffect(() => {
        dispatch(getAboutUser({ token: localStorage.getItem("token") }))
        dispatch(getAllPosts());
    }, [])

    useEffect(() => {
        if (authState.user != undefined) {
            setUserProfile(authState.user)
            let post = postState.posts.filter((post) => {
                return post.user.username === authState.user.username
            })
            setUserPosts(post)
        }

    }, [authState.user, postState.posts])





    const updateProfilePicture = async (file) => {
        const formData = new FormData();
        formData.append("file", file);

        const response = await clientServer.post("/user/update_profile_picture", formData, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "multipart/form-data",
            },
        });
        dispatch(getAboutUser({ token: localStorage.getItem("token") }))
    }


    const handleUpdate = async () => {
        const request = await clientServer.post("/user/user_update", {
            name: userProfile.name,
            email: userProfile.email
        }, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        })

        const response = await clientServer.post("/user/update_profile_data", {
            bio: userProfile.bio,
            currentPost: userProfile.currentPost,
            pastWork: userProfile.pastWork,
            education: userProfile.education

        }, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        })
        await dispatch(getAboutUser({ token: localStorage.getItem("token") }))
    }






    return (
        <UserLayout>
            <DashBoardLayout>
                {authState.user && userProfile.id &&
                    <div className={styles.container}>
                        <div className={styles.backdropContainer}>
                            <label htmlFor="profilePictureUpload" className={styles.backdrop__overlay}>
                                <p>
                                    Edit
                                </p>
                            </label>
                            <input onChange={(e) => {
                                updateProfilePicture(e.target.files[0])
                            }} hidden type="file" id="profilePictureUpload" />
                            <img className={styles.backdrop} src={userProfile.profilePicture} alt="ProfilePicture" />
                        </div>
                        <div className={styles.profileContainer_details}>
                            <div className={styles.profileContainer_flex}>

                                <div style={{ flex: "0.8" }}>


                                    <div style={{ display: "flex", width: "fit-content", alignItems: "center", gap: "1.2rem" }}>
                                        <input className={styles.nameEdit} type="text" value={userProfile.name} onChange={(e) => {
                                            setUserProfile({ ...userProfile, name: e.target.value });
                                        }} />
                                        <p contentEditable style={{ color: "grey" }}>{userProfile.username}</p>
                                    </div>



                                    <div>
                                        <textarea value={userProfile.bio}
                                            onChange={(e) => {
                                                setUserProfile({ ...userProfile, bio: e.target.value })
                                            }}
                                            rows={Math.max(3, Math.ceil(userProfile.bio.length / 80))}
                                            style={{ width: "100%" }}
                                        />
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
                            <h4>Work History</h4>
                            <div className={styles.workHistoryContainer}>
                                {
                                    userProfile.pastWork.map((work, index) => {
                                        return (
                                            <div key={index} className={styles.workHistoryCard}>
                                                <p style={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: "0.8rem" }}>
                                                    {work.company}-{work.position}
                                                </p>
                                                <p>{work.year}</p>

                                            </div>
                                        )
                                    })
                                }

                            </div>
                            <button className={styles.addWorkButton} onClick={() => setIsModalOpen(true)} >Add Work</button>
                        </div>



                        <div className="eduHistory">
                            <h4>Education</h4>
                            <div className={styles.eduHistoryContainer}>
                                {
                                    userProfile.education.map((edu, index) => {
                                        return (
                                            <div key={index} className={styles.eduHistoryCard}>
                                                <p style={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: "0.8rem" }}>
                                                    {edu.school}-{edu.degree}
                                                </p>
                                                <p>{edu.fieldOfStudy}</p>

                                            </div>
                                        )
                                    })
                                }

                            </div>
                            <button className={styles.addWorkButton} onClick={() => setIsEduModalOpen(true)} >Add Education</button>
                        </div>

                        {userProfile != authState.user && <div onClick={() => {
                            handleUpdate()
                        }} className={styles.updateProfileBtn}>
                            Update Profile
                        </div>}


                    </div>
                }



                {isModalOpen &&
                    <div onClick={() => {
                        setIsModalOpen(false)
                    }} className={styles.commentsContainer}>

                        <div onClick={(e) => e.stopPropagation()} className={styles.allCommentsConatiner}>

                            <input onChange={handleWorkInputChange} name="company"
                                className={styles.inputField} placeholder="Past/Current - Company Name" type="text" />


                            <input onChange={handleWorkInputChange} name="position"
                                className={styles.inputField} placeholder="Enter Postion" type="text" />


                            <input onChange={handleWorkInputChange} name="year"
                                className={styles.inputField} placeholder="Years" type="number" />

                            <div
                                onClick={() => {
                                    setUserProfile({
                                        ...userProfile,
                                        pastWork: [...(userProfile.pastWork), inputData],
                                    });
                                    setIsModalOpen(false);
                                }}
                                className={styles.updateProfileBtn}>Add</div>


                        </div>
                    </div>
                }



                {
                    isEduModalOpen &&
                    <div onClick={() => {
                        setIsEduModalOpen(false)
                    }} className={styles.commentsContainer}>

                        <div onClick={(e) => e.stopPropagation()} className={styles.allCommentsConatiner}>

                            <input onChange={handleEduInputChange} name="school"
                                className={styles.inputField} placeholder="Enter School Name" type="text" />


                            <input onChange={handleEduInputChange} name="degree"
                                className={styles.inputField} placeholder="Enter Degree" type="text" />


                            <input onChange={handleEduInputChange} name="fieldOfStudy"
                                className={styles.inputField} placeholder="Enter Field" type="text" />

                            <div
                                onClick={() => {
                                    setUserProfile({
                                        ...userProfile,
                                        education: [...(userProfile.education), eduInputData],
                                    });
                                    setIsEduModalOpen(false);
                                }}
                                className={styles.updateProfileBtn}>Add</div>


                        </div>
                    </div>
                }
            </DashBoardLayout>
        </UserLayout>
    )
}