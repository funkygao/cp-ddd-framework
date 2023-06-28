" ~/.vim/plugin/reverse_model.vim
" :1,$ call HighlightBrackets()
highlight AngleBrackets ctermfg=green
highlight SquareBrackets ctermfg=cyan
highlight DashBrackets ctermfg=Magenta

augroup highlight_brackets
  autocmd!
  autocmd BufNewFile,BufRead * call HighlightBrackets()
augroup END

function! HighlightBrackets()
  let line_num = line('.')
  let line_content = getline(line_num)

  let angle_pattern = '\v\<\<(.+)\>\>'
  let angle_match_item = matchlist(line_content, angle_pattern)
  if angle_match_item != []
    let start_pos = angle_match_item[0]
    call matchadd("AngleBrackets", start_pos)
  endif

  let square_pattern = '\v\[(.+)\]'
  let square_match_item = matchlist(line_content, square_pattern)
  if square_match_item != []
    let start_pos = square_match_item[0]
    call matchadd("SquareBrackets", start_pos)
  endif

  let dash_pattern = '\v\-(.+)\-'
  let dash_match_item = matchlist(line_content, dash_pattern)
  if dash_match_item != []
    let start_pos = dash_match_item[0]
    call matchadd("DashBrackets", start_pos)
  endif
endfunction
