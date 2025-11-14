import { createSlice } from "@reduxjs/toolkit"
import { getAboutUser, getAllUsers, getConnectionsRequest, getMyConnectionRequest, loginUser, registerUser } from "../../action/authAction"


const initialState = {
    user: undefined,
    isError: false,
    isSuccess: false,
    loggedIn: false,
    isLoading:false,
    message: "",
    isTokenThere:false,
    profileFetched: false,
    connections: [],
    connectionRequest: [],
    all_profiles_fetched:false,
    all_profiles:[],
}




const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        reset: () => initialState,
        handleLoginUser: (state) => {
            state.message = "hello"
        },
        emptyMessage: (state) => {
            state.message = ""
        },
        setTokenThere:(state)=>{
            state.isTokenThere=true
        },
        setTokenNotThere:(state)=>{
            state.isTokenThere=false;
        }

    },
    extraReducers: (builder) => {
        builder.addCase(loginUser.pending, (state) => {
            state.isLoading = true
            state.message = "knocking the door..."
        })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.isLoading = false;
                state.isError = false;
                state.isSuccess = true;
                state.loggedIn = true;
                state.message = "Login Is Successfull"
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.isLoading = false;
                state.isError = true;

                if (action.payload && typeof action.payload === "object" && action.payload.error) {
                    state.message = action.payload.error;
                } else {
                    state.message = action.payload || "Login failed";
                }
            })
            .addCase(registerUser.pending, (state) => {
                state.isLoading = true
                state.message = "Registering You..."
            })
            .addCase(registerUser.fulfilled, (state, action) => {
                state.isLoading = false;
                state.isError = false;
                state.isSuccess = true;
                state.message = "Register Is Successfull, Please Sign In"
            })
            .addCase(registerUser.rejected, (state, action) => {
                state.isLoading = false;
                state.isError = true;
                if (action.payload && typeof action.payload === "object" && action.payload.error) {
                    state.message = action.payload.error;
                } else {
                    state.message = action.payload || "Registration failed,Please Sign Up Again";
                }
            })
            .addCase(getAboutUser.fulfilled, (state, action) => {
                state.user = action.payload;
                state.isError = false;
                state.isLoading=false;
                state.profileFetched = true;
            })
            .addCase(getAllUsers.fulfilled,(state,action)=>{
                state.isError = false;
                state.isLoading=false;
                state.all_profiles_fetched=true;
                state.all_profiles=action.payload;
            })
            .addCase(getConnectionsRequest.fulfilled,(state,action)=>{
                state.connections=action.payload
            })
            .addCase(getConnectionsRequest.rejected,(state,action)=>{
                state.message=action.payload
            })
            .addCase(getMyConnectionRequest.fulfilled,(state,action)=>{
                state.connectionRequest=action.payload
            })
            .addCase(getMyConnectionRequest.rejected,(state,action)=>{
                state.message=action.payload
            })
    }
})


export const { reset, emptyMessage,setTokenThere,setTokenNotThere } = authSlice.actions

export default authSlice.reducer