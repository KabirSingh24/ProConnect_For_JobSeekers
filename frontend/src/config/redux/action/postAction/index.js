
import { clientServer } from "@/config";
import { createAsyncThunk } from "@reduxjs/toolkit";



export const getAllPosts = createAsyncThunk(
    "post/getAllPosts",
    async (_, thunkAPI) => {
        try {
            const response = await clientServer.get("/posts/getAllPosts")
            return thunkAPI.fulfillWithValue(response.data)
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response.data)
        }
    }
)


export const createPost = createAsyncThunk(
    "posts/createPost",
    async ({ post, token }, thunkAPI) => {
        try {
            const formData = new FormData();
            formData.append("body", post.body);
            if (post.file) {
                formData.append("file", post.file);
            }

            const response = await clientServer.post("/posts/create_post", formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data",
                },
            });

            if (response.status === 200) {
                return thunkAPI.fulfillWithValue("Post Uploaded");
            } else {
                return thunkAPI.rejectWithValue("Post not Uploaded");
            }
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Something went wrong");
        }
    }
);



export const deletePost = createAsyncThunk(
    "posts/deletePost",
    async ({ postId, token }, thunkAPI) => {
        try {
            const response = await clientServer.post(
                `/posts/${postId}/deletePost`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            return thunkAPI.fulfillWithValue(response.data);
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Delete failed");
        }
    }
);


export const incrementLike = createAsyncThunk(
    "post/incrementLike",
    async (pId, thunkAPI) => {
        try {
            const response = await clientServer.post(`/posts/${pId}/likeIncrement`);
            return thunkAPI.fulfillWithValue(response.data);
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Like failed");
        }
    }
)

export const getAllComments = createAsyncThunk(
    "post/getAllComments",
    async ({ postId }, thunkAPI) => {
        try {
            const response = await clientServer.get(`/posts/${postId}/getComments`);
            return thunkAPI.fulfillWithValue({
                comments: response.data,
                postId,
            });
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Post failed");
        }
    }
)

export const postComment = createAsyncThunk(
    "post/postComment",
    async ({ postId, body, token }, thunkAPI) => {
        console.log(postId);
        try {
            const response = await clientServer.post(
                `/posts/${postId}/comment`,
                null,
                {
                    params: { body },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            return response.data;
        } catch (err) {
            return thunkAPI.rejectWithValue(err.response?.data || "Comment failed");
        }
    }
);

export const deleteComment = createAsyncThunk(
    "post/deleteComment",
    async ({ commentId, token }) => {
        await clientServer.post(
            `/posts/${commentId}/deleteComment`,
            {},
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
        return commentId;
    }
);
