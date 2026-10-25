package com.codecrafthub.service;

import com.codecrafthub.model.Course;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {
    private static final String FILE_PATH = "courses.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CourseService() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                objectMapper.writeValue(file, new ArrayList<Course>());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to initialize storage file: " + FILE_PATH, e);
            }
        }
    }

    private synchronized List<Course> readCourses() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Course>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private synchronized void writeCourses(List<Course> courses) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), courses);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write data to storage file", e);
        }
    }

    public List<Course> getAllCourses() {
        return readCourses();
    }

    public Optional<Course> getCourseById(Long id) {
        return readCourses().stream()
                .filter(course -> course.getId() != null && course.getId().equals(id))
                .findFirst();
    }

    public Course createCourse(Course course) {
        List<Course> courses = readCourses();
        long nextId = courses.stream()
                .map(Course::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L) + 1L;

        course.setId(nextId);
        course.setCreatedAt(LocalDateTime.now().toString());
        courses.add(course);
        writeCourses(courses);
        return course;
    }

    public Optional<Course> updateCourse(Long id, Course updatedCourse) {
        List<Course> courses = readCourses();
        for (Course existing : courses) {
            if (existing.getId() != null && existing.getId().equals(id)) {
                if (updatedCourse.getName() != null) {
                    existing.setName(updatedCourse.getName());
                }
                if (updatedCourse.getDescription() != null) {
                    existing.setDescription(updatedCourse.getDescription());
                }
                if (updatedCourse.getTargetDate() != null) {
                    existing.setTargetDate(updatedCourse.getTargetDate());
                }
                if (updatedCourse.getStatus() != null) {
                    existing.setStatus(updatedCourse.getStatus());
                }
                writeCourses(courses);
                return Optional.of(existing);
            }
        }
        return Optional.empty();
    }

    public boolean deleteCourse(Long id) {
        List<Course> courses = readCourses();
        boolean removed = courses.removeIf(course -> course.getId() != null && course.getId().equals(id));
        if (removed) {
            writeCourses(courses);
        }
        return removed;
    }
}