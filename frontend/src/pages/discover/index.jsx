import { getAllUsers } from "@/config/redux/action/authAction"
import DashBoardLayout from "@/layout/DashBoardLayout"
import UserLayout from "@/layout/UserLayout"
import React, { useEffect } from "react"
import { useDispatch, useSelector } from "react-redux"
import styles from "./styles.module.css"
import { useRouter } from "next/router"

export default function DiscoverPage() {

    const authState = useSelector((state) => state.auth)
    const router=useRouter();

    const dispatch = useDispatch();

    useEffect(() => {
        if (!authState.all_profiles_fetched) {
            dispatch(getAllUsers())
        }
    }, [])



    return (
        <UserLayout>
            <DashBoardLayout>
                <div>
                    <h1>DisCover</h1>
                    <div className={styles.allUserProfile}>

                        {authState.all_profiles_fetched && authState.all_profiles.map((user) => {
                            return (
                                <div onClick={()=>{
                                    router.push(`/view_profile/${user.profile.user.username}`)
                                }} key={user.profile.user._id} className={styles.userCard}>
                                    <img className={styles.userCrad_image} src={user.profile.user.profilePicture} alt="profile" />
                                    <div className={styles.us}>
                                        <h1>{user.profile.user.name}</h1>
                                        <p>{user.profile.user.username}</p>
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