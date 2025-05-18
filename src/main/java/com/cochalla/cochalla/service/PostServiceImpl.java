package com.cochalla.cochalla.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.dto.CommentDto;
import com.cochalla.cochalla.dto.PostDto;
import com.cochalla.cochalla.dto.PostPageDto;
import com.cochalla.cochalla.repository.CommentRepository;
import com.cochalla.cochalla.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public PostPageDto get(Integer postId) {
        Optional<PostDto> optPost = postRepository.findPostById(postId);
        PostDto postDto = optPost.orElseThrow();

        Sort sort = Sort.by(Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 1, sort);        
        
        Page<CommentDto> comments = commentRepository.findCommentsByPostId(postDto.getPostId(), pageable);

        return new PostPageDto(postDto, comments.toList());
    }

    @Override
    public Boolean delete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Boolean togglePublic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'togglePublic'");
    }
    
}
