@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {
    private final EmployeeProfileService service;

    public EmployeeProfileController(EmployeeProfileService service) {
        this.service = service;
    }

    // ONLY ADMIN can create employees
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<EmployeeProfileDto> create(@RequestBody EmployeeProfileDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // BOTH ADMIN and USER can view by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EmployeeProfileDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // BOTH ADMIN and USER can view by Team
    @GetMapping("/team/{teamName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EmployeeProfileDto>> getByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(service.getByTeam(teamName));
    }
}