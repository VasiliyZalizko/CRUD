package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final ConcurrentHashMap<Long, Post> postMap = new ConcurrentHashMap<>();

    public List<Post> all() {
        if (postMap.isEmpty()) {
            throw new NotFoundException("Список постов пуст");
        }
        List<Post> data = new ArrayList<>();
        for (Map.Entry<Long, Post> entry : postMap.entrySet()) {
            data.add(entry.getValue());
        }
        return data;
    }

    public Optional<Post> getById(long id) {
        if (postMap.containsKey(id)) {
            return Optional.ofNullable(postMap.get(id));
        }
        throw new NotFoundException("Неверный идентификатор поста");
    }

    public Post save(Post post) {
        Post tempPost;
        if (post.getId() == 0) {
            counter.addAndGet(1);
            postMap.put(counter.get(), new Post(post.getContent()));
            tempPost = postMap.get(counter.get());
        } else if (postMap.containsKey(post.getId())) {
            postMap.replace(post.getId(), postMap.get(post.getId()), post);
            tempPost = post;
        } else {
            throw new NotFoundException("There is no post with specified id");
        }
        return tempPost;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}