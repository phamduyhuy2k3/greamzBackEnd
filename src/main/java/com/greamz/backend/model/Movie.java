package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idM;
    private String name;
    private String thumbnail;
    @Embedded
    private Webm webm;
    @Embedded
    private Mp4 mp4;
    private boolean highlight;
    @ManyToOne
    private GameModel gameModel;

    public static class Webm {

        private String webm480;
        private String webmMax;

        public Webm() {}

        public Webm(String webm480, String webmMax) {
            this.webm480 = webm480;
            this.webmMax = webmMax;
        }

        public String getWebm480() {
            return webm480;
        }

        public void setWebm480(String webm480) {
            this.webm480 = webm480;
        }

        public String getWebmMax() {
            return webmMax;
        }

        public void setWebmMax(String webmMax) {
            this.webmMax = webmMax;
        }
    }

    public static class Mp4 {

        private String mp4480;
        private String mp4Max;

        public Mp4() {}

        public Mp4(String mp4480, String mp4Max) {
            this.mp4480 = mp4480;
            this.mp4Max = mp4Max;
        }

        public String getMp4480() {
            return mp4480;
        }

        public void setMp4480(String mp4480) {
            this.mp4480 = mp4480;
        }

        public String getMp4Max() {
            return mp4Max;
        }

        public void setMp4Max(String mp4Max) {
            this.mp4Max = mp4Max;
        }
    }
}
