package com.tanzdata.tanzdata.controller;


import com.tanzdata.tanzdata.models.RedditComments;
import com.tanzdata.tanzdata.repo.RedditCommentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.*;


@RestController
@RequestMapping("/")
public class Index {

    @Autowired
    RedditCommentsRepo redditCommentsRepo;

    @GetMapping("test")
    public String testCart(){
        String file = "/media/astra/New Volume/traing data/RC_2021-06";
        int totalCount = 208027069;
//        String file = "/media/astra/New Volume/traing data/RC_2021-06";
        try {

            FileInputStream fis=new FileInputStream(file);
            Scanner sc=new Scanner(fis);  //file to be scanned
            int count = 0;
            int count2 = 0;
            List<RedditComments> redditCommentsList = new ArrayList<>();
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] data = line.substring(1,line.length()).trim().split(",");
                HashMap<String, Object> hashMap = new HashMap<>();
                for (int i = 0; i< data.length; i++){
                    if (data[i].split(":").length == 2){
                        hashMap.put(data[i].split(":")[0].replaceAll("\"", ""),data[i].split(":")[1]);
                    }
                }
                String body = (String) hashMap.get("body");
                String parentId = (String) hashMap.get("parent_id");
                int score = Integer.parseInt((String) hashMap.get("score"));
                String subReddit = (String) hashMap.get("subreddit");
                String commentId = (String) hashMap.get("name");

                RedditComments redditComments = new RedditComments();
                if (score >= 4 && acceptable(body)) {
                    Optional<RedditComments> comments = findParent(parentId);

                    Optional<RedditComments> scoreData= findExistingCommentScore(parentId);

                    if (scoreData.isPresent()){
                        int existingCommentScore = scoreData.get().getScore();
                        if (score > existingCommentScore){
                            insertReplaceComment();
                        }
                    }else {
                        if(comments.get().getBody()!=null){
                            insertHasParent();
                        }else{
                            insertNoParent();
                        }
                    }

//
//                    if (comments.isPresent()){
//                        String parentData = comments.get().getBody();
//                        redditComments.setParent(parentData);
//                    }
                    redditComments.setBody(body);
                    redditComments.setParentId(parentId);
                    redditComments.setScore(score);
                    redditComments.setSubReddit(subReddit);
                    redditComments.setCommentId(commentId);
                    redditCommentsList.add(redditComments);
                    if (count == 5000){
                        redditCommentsList = redditCommentsRepo.saveAll(redditCommentsList);
//                        redditCommentsRepo.save(redditComments);
                        redditCommentsRepo.saveAll(redditCommentsList);
                        System.out.println("stored 10000" +  "-----" + String.valueOf(count2) );
                        System.out.println("-----------------------------");
                        totalCount = totalCount - count2;
                        System.out.println(totalCount);
                        System.out.println("-----------------------------");
                        redditCommentsList = new ArrayList<>();
                        count = 0;
                    }
                    count = count +1;
                    count2 = count2 +1;
                }
            }
            sc.close();

        }catch (Exception e){
            e.getStackTrace();
        }
        return "all looks good";
    }

    private boolean acceptable(String body) {
        if (body == null)
            return false;
        else if (body.length() > 1000) {
            return false;
        } else if (body.equals("[deleted]") || body.equals("[removed]")) {
            return false;
        } else if (body.split(" ").length >50 || body.split(" ").length < 1) {
            return false;
        } else {
            return true;
        }
    }

    private Optional<RedditComments> findExistingCommentScore(String parentId) {
        return redditCommentsRepo.findFirstByParentId(parentId);
    }

    private Optional<RedditComments> findParent(String parentId) {
        return redditCommentsRepo.findFirstByCommentId(parentId);
    }
}
