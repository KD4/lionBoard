package com.github.kd4.lionboard.service;

import com.github.kd4.lionboard.dao.CommentStatusDao;
import com.github.kd4.lionboard.dao.PostStatusDao;
import com.github.kd4.lionboard.domain.CommentStatus;
import com.github.kd4.lionboard.domain.PostStatus;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by daum on 16. 2. 22..
 */
public class StatusBatchService extends QuartzJobBean {

    private static final Logger logger =
            LoggerFactory.getLogger(StatusBatchService.class);

    PostStatusDao postStatusDao;

    CommentStatusDao commentStatusDao;

    public void setPostStatusDao(PostStatusDao postStatusDao) {
        this.postStatusDao = postStatusDao;
    }

    public void setCommentStatusDao(CommentStatusDao commentStatusDao) {
        this.commentStatusDao = commentStatusDao;
    }

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("Start process to update the status on post at "+now("yyyy.MM.dd G 'at' hh:mm:ss"));
        List<Integer> updatedPosts = new ArrayList<>();
        try {
            List<PostStatus> postStatusList = postStatusDao.getAll();
            postStatusList.forEach(postStatus -> updatePostStatus(postStatus, updatedPosts));
        }catch (RuntimeException re){
            re.printStackTrace();
        }
        logger.debug("Update the Status of Posts :"+updatedPosts.size());
        for(Integer postId:updatedPosts){
            logger.debug("updated post id is :"+postId);
        }

        logger.debug("Start process to update the status on comments at "+now("yyyy.MM.dd G 'at' hh:mm:ss"));
        List<Integer> updatedCmt = new ArrayList<>();
        try {
            List<CommentStatus> commentStatusList = commentStatusDao.getAll();
            commentStatusList.forEach(cmtStatus -> updateCmtStatus(cmtStatus, updatedCmt));
        }catch (RuntimeException re){
            re.printStackTrace();
        }
        logger.debug("Update the Status of comments :"+updatedCmt.size());
        for(Integer cmtId:updatedCmt){
            logger.debug("updated comment id is :"+cmtId);
        }




    }



    private void updateCmtStatus(CommentStatus commentStatus, List<Integer> updatedCmt){
        int pastDays = commentStatus.getPastDays() + 1;
        String status = commentStatus.getCmtStatus();
        // 게시물 상태가 T(Temp) 인채로 30일 이상 지속되면 상태를 "A"(Admin Delete)로 바꾼다.
        if(status.equals("T") && pastDays > 30){
            pastDays = 1;
            status = "A";
            updatedCmt.add(commentStatus.getCmtId());
        }
        commentStatus.setPastDays(pastDays);
        commentStatus.setCmtStatus(status);
        commentStatusDao.update(commentStatus);
    }

    private void updatePostStatus(PostStatus postStatus, List<Integer> updatedPosts){
        int pastDays = postStatus.getPastDays() + 1;
        String status = postStatus.getPostStatus();
        // 게시물 상태가 T(Temp) 인채로 30일 이상 지속되면 상태를 "A"(Admin Delete)로 바꾼다.
        if(status.equals("T") && pastDays > 30){
            pastDays = 1;
            status = "A";
            updatedPosts.add(postStatus.getPostId());
        }
        postStatus.setPastDays(pastDays);
        postStatus.setPostStatus(status);
        postStatusDao.update(postStatus);

    }
}
