package com.zhonghua.zhonghuaspetitions.service;

import com.zhonghua.zhonghuaspetitions.model.Petition;
import com.zhonghua.zhonghuaspetitions.model.Signature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetitionService {
    private final List<Petition> petitions = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public PetitionService() {
        // sample data to start with
        Petition p1 = new Petition(idSeq.getAndIncrement(), "Save the Community Park",
                "We want more funding to maintain the community park and playground.", "Alice");
        p1.addSignature(new Signature("Bob", "bob@example.com"));
        p1.addSignature(new Signature("Carol", "carol@example.com"));

        Petition p2 = new Petition(idSeq.getAndIncrement(), "Extend Library Hours",
                "Extend the opening hours of the library on weekends.", "David");
        p2.addSignature(new Signature("Eve", "eve@example.com"));

        Petition p3 = new Petition(idSeq.getAndIncrement(), "Improve Bus Service",
                "More frequent buses are needed during peak hours.", "Frank");

        petitions.add(p1);
        petitions.add(p2);
        petitions.add(p3);
    }

    public List<Petition> findAll() {
        return petitions;
    }

    public Petition create(String title, String description, String author) {
        Petition p = new Petition(idSeq.getAndIncrement(), title, description, author);
        petitions.add(p);
        return p;
    }

    public Optional<Petition> findById(Long id) {
        return petitions.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Petition> searchByTitle(String q) {
        String lower = q == null ? "" : q.toLowerCase();
        List<Petition> res = new ArrayList<>();
        for (Petition p : petitions) {
            if (p.getTitle() != null && p.getTitle().toLowerCase().contains(lower)) {
                res.add(p);
            }
        }
        return res;
    }

    public void sign(Long petitionId, String name, String email) {
        findById(petitionId).ifPresent(p -> p.addSignature(new Signature(name, email)));
    }

}
