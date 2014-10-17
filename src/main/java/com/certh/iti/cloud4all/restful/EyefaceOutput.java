package com.certh.iti.cloud4all.restful;

import java.util.ArrayList;

/**
 *
 * @author nkak
 */
public class EyefaceOutput 
{
    String status;
    int error_code;
    String error_message;
    ArrayList<photo> photos;    
    class photo
    {
        int width;
        int height;
        String url;
        ArrayList<tag> tags;
        
        class tag
        {
            float yaw;
            float width;
            float height;
            Center center;
            class Center
            {
                float x;
                float y;
            }
            attributes attributes;
            class attributes
            {
                age_est age_est;
                class age_est
                {
                    int value;
                }
                gender gender;
                class gender
                {
                    String value;
                }
            }
            eye_left_corner_left eye_left_corner_left;
            class eye_left_corner_left
            {
                float x;
                float y;
            }
            eye_left_corner_right eye_left_corner_right;
            class eye_left_corner_right
            {
                float x;
                float y;
            }
            eye_right_corner_left eye_right_corner_left;
            class eye_right_corner_left
            {
                float x;
                float y;
            }
            eye_right_corner_right eye_right_corner_right;
            class eye_right_corner_right
            {
                float x;
                float y;
            }
            mouth_left mouth_left;
            class mouth_left
            {
                float x;
                float y;
            }
            mouth_right mouth_right;
            class mouth_right
            {
                float x;
                float y;
            }
            nose nose;
            class nose
            {
                float x;
                float y;
            }
            eye_left eye_left;
            class eye_left
            {
                float x;
                float y;
            }
            eye_right eye_right;
            class eye_right
            {
                float x;
                float y;
            }
            mouth_center mouth_center;
            class mouth_center
            {
                float x;
                float y;
            }
            int tid;
        }
    }
}
