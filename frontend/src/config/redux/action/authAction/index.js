import { clientServer } from "@/config";
import { createAsyncThunk } from "@reduxjs/toolkit";





export const loginUser = createAsyncThunk(
    "user/login",
    async (user, thunkAPI) => {
        try {
            const response = await clientServer.post("/user/login", {
                email: user.email,
                password: user.password
            });
            if (response.data) {
                localStorage.setItem("token", response.data);
            } else {
                return thunkAPI.rejectWithValue({
                    message: "token not provided"
                })
            }
            return thunkAPI.fulfillWithValue(response.data);
        } catch (err) {
            console.log("Error response:", err.response.data);
            return thunkAPI.rejectWithValue(err.response.data)
        }

    }
)

export const registerUser = createAsyncThunk(
    "user/register",
    async (user, thunkAPI) => {
        try {
            const response = await clientServer.post("/user/register", {
                name: user.name,
                username: user.username,
                email: user.email,
                password: user.password,
            });
            return thunkAPI.fulfillWithValue(response.data);

        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)


export const getAboutUser = createAsyncThunk(
    "user/getAboutUser",
    async (user, thunkAPI) => {
        try {
            const response = await clientServer.get("/user/get_user_and_profile", {
                headers: {
                    Authorization: `Bearer ${user.token}`,
                },
            })
            return thunkAPI.fulfillWithValue(response.data);

        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)

export const getAllUsers = createAsyncThunk(
    "user/getAllUser",
    async (_, thunkAPI) => {
        try {
            const response = await clientServer.get("/user/get_all_users")
            return thunkAPI.fulfillWithValue(response.data)
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)

export const sendConnectionRequest = createAsyncThunk(
    "user/sendConnectionRequest",
    async ({ token, userId }, thunkAPI) => {
        try {
            const response = await clientServer.post((`/user/${userId}/send_connection_request`), {}, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            thunkAPI.dispatch(getConnectionsRequest(token))
            return thunkAPI.fulfillWithValue(response.data);
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)

export const getConnectionsRequest = createAsyncThunk(
    "user/getConnectionsRequest",
    async (token, thunkAPI) => {
        try {

            const response = await clientServer.get(("/user/get_connection_request"), {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            return thunkAPI.fulfillWithValue(response.data)
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)

export const getMyConnectionRequest = createAsyncThunk(
    "user/getMyConnectionRequest",
    async (token, thunkAPI) => {
        try {

            const response = await clientServer.get(("/user/what_are_my_connections"), {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            return thunkAPI.fulfillWithValue(response.data)
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)

export const AcceptConnection = createAsyncThunk(
    "user/AcceptConnection",
    async ({ requestId, token, action }, thunkAPI) => {
        try {
            const response = await clientServer.post(
                `/user/update_connection_status/${requestId}?action=${action}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            thunkAPI.dispatch(getConnectionsRequest(token))
            thunkAPI.dispatch(getMyConnectionRequest(token))
            return thunkAPI.fulfillWithValue(response.data);
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Something went wrong");
        }
    }
);
