</button>
<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>
    <li role='presentation'>
    <a class='user-status-change' data-userId='$user.id' data-username='$user.name' data-status='A' role='menuitem' tabindex='-1' href='#'>Active</a>
    </li>
    <li role='presentation'>
    <a class='user-status-change' data-userId='$user.id' data-username='$user.name' data-status='B' role='menuitem' tabindex='-1' href='#'>Block</a>
    </li>
    </ul>
    </div>
    </td>
    <td>
    <div class='dropdown'>
    <button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>
    $user.roles
    <span class='caret'></span>
    </button>
    <ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>
    <li role='presentation'><a class='user-role-change' data-userId='$user.id' data-username='$user.name' data-roles='ROLE_USER' role='menuitem' tabindex='-1' href='#'>사용자</a></li>
<li role='presentation'><a class='user-role-change' data-userId='$user.id' data-username='$user.name' data-roles='ROLE_MANAGER' role='menuitem' tabindex='-1' href='#'>관리자</a></li>
<li role='presentation'><a class='user-role-change' data-userId='$user.id' data-username='$user.name' data-roles='ROLE_ADMIN' role='menuitem' tabindex='-1' href='#'>마스터</a></li>
</ul>
</div>
</td>
</tr>