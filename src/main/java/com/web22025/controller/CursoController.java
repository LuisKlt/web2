package com.web22025.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web22025.dto.CursoDTO;
import com.web22025.model.Curso;
import com.web22025.repository.CursoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {
    @Autowired
    CursoRepository repository;
    @GetMapping("/inserir")
    public String inserir() {
        return "curso/inserir";
    }

    @PostMapping("/inserir")
    public String inserido(
            @ModelAttribute @Valid CursoDTO dto,
            BindingResult result, RedirectAttributes msg
    ) {
        if(result.hasErrors()) {
            msg.addFlashAttribute("erro","ERRO");
            return "redirect:/curso/inserir";
        }
        var curso = new Curso();
        BeanUtils.copyProperties(dto, curso);
        repository.save(curso);
        msg.addFlashAttribute("sucesso", "Curso cadastrada!");
        return "redirect:/curso/inserir";
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("curso/listar");
        List<Curso> lista = repository.findAll();
        mv.addObject("cursos", lista);
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable(value="id") int id) {
        Optional<Curso> curso = repository.findById(id);
        if (curso.isEmpty()){
            return "redirect:/curso/listar";
        }
        repository.deleteById(id);
        return "redirect:/curso/listar";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable(value="id") int id){
        ModelAndView mv = new ModelAndView("curso/editar");
        Optional<Curso> curso =repository.findById(id);
        mv.addObject("id", curso.get().getID());
        mv.addObject("nome", curso.get().getNome());
        return mv;
    }
    @PostMapping("/editar/{id}")
    public String editado(@ModelAttribute @Valid CursoDTO dto,
                          BindingResult result, RedirectAttributes msg,
                          @PathVariable(value = "id")int id){
        if (result.hasErrors()){
            msg.addFlashAttribute("erro","Erro ao editar");
            return "redirect:/curso/listar";
        }
        Optional<Curso> cat = repository.findById(id);

        var curso = cat.get();
        BeanUtils.copyProperties(dto, curso);
        repository.save(curso);
        msg.addFlashAttribute("sucesso", "Curso editada!");
        return "redirect:/curso/listar";
    }
}
